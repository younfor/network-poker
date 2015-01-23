package com.hj.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;
import com.hj.tool.Card;
import com.hj.tool.Comm;

import android.util.Log;

public class Server extends Thread implements Disposable{

	Constant con=Constant.getCons();
	public boolean stop=false;
	public static Server s=null;
	public static Server getServer(){if(s==null) {s=new Server();s.start();}return s;}
	public void run() {
		// 消息循环
	DatagramSocket server=null;
	 try {  
         //定义UDP监听  
         server = new DatagramSocket(Proxys.PORT_RECEIVE + con.Max);  
         //定义缓冲区  
         byte[] buffer=new byte[1024];  
         //定义接收数据包  
         DatagramPacket packet=new DatagramPacket(buffer,buffer.length);  
         while (!stop) {  
             //接收数据  
             server.receive(packet);  
             //判断是否收到数据，然后输出字符串  
             if(packet.getLength()>0){  
                 String str = new String(buffer,0,packet.getLength());  
                 //out(str);
                 parse(str.split(Proxys.div)); 
             }  
         }  
     } catch (SocketException e) {  
         e.printStackTrace();  
     } catch (IOException e) {  
         e.printStackTrace();  
     } finally{
    	 server.close();
     }
	}
	public void parse(String data[])
	{
		int cmd=Integer.parseInt(data[1]);
		Constant c= Constant.getCons();
		switch(cmd)
		{
			case Proxys.CMD_HOST_REQUEST_ROOM:// 搜索桌子
				c.u = new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				if (c.currentRoom!=null &&c.me.getIp().equals(c.currentRoom.getIp())) {
					out("有人进房间:"+c.u.getIp());
					c.replyRoom(c.u);
				}
				break;
			case Proxys.CMD_REPLY_ROOM: // 收到反馈桌子
			case Proxys.CMD_CREATE_ROOM: // 收到创建的桌子
				c.r = new Room(data[3],Integer.parseInt(data[4]));
				if (!c.isContainsRoom(c.r)) {
					c.roomList.add(c.r);
					c.updateRoomList();
				}
				break;
			case Proxys.CMD_HOST_JOIN_ROOM: // 收到请求加入桌子
				c.u =new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				out("请求加入:"+c.u.getIp());
				if (!c.isContainsUser(c.u)) {
					c.users.add(c.u);
					c.currentRoom.userCount++;
					c.orderComputer();
					c.broadUser();
					c.updateRoom();
				}
				break;
			case Proxys.CMD_BROAD_USERLIST:// 收到用户列表更新
				out("用户更新");
				int len=(data.length-4)/6;
				out("len="+len);
				c.users.clear();
				for(int i=0;i<len;i++)
				{
					c.users.add(new User(data[3+i*6],data[4+i*6],Integer.parseInt(data[5+i*6]),Integer.parseInt(data[6+i*6]),
							Integer.parseInt(data[7+i*6]),Integer.parseInt(data[8+i*6])));
				}
				c.updateUserList();
				break;
			case Proxys.CMD_HOST_READY_ROOM: // 准备
				
				c.u =new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				out("准备:"+c.u.getDeskId());
				for (User user : c.users) {
					if (user.getIp().equals(c.u.getIp()))
						user.setIsReady(1);
				}
				c.broadUser();
				// 开始游戏
				c.beginGame();
				break;
			case Proxys.CMD_UPDATE_ROOM: // 收到桌子列表更新
				c.r = new Room(data[3],Integer.parseInt(data[4]));
				if (c.isContainsRoom(c.r)) {
					for (Room t : c.roomList) {
						if (t.getIp().equals(c.r.getIp()))
							t.setUserCount(c.r.getUserCount());
					}
				} else
					c.roomList.add(c.r);
				c.updateRoomList();
				break;
			case Proxys.CMD_BEGIN_CARDS:// 收到发的牌0
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				c.updateBeginCard();
				c.startLandlord();
				break;
			case Proxys.CMD_BEGIN_LANDLORD_CARDS: // 收到地主牌
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				c.endHostCard();
				break;
			case Proxys.CMD_HOST_FINISH_LANDLORD: // 按下了抢分按钮
				c.u =new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				out("抢分:"+c.u.getDeskId());
				// 判断抢分完成没有
				if (!con.judgeFinishLandlord())
					con.nextLandlord(c.u.getDeskId());
				break;
			case Proxys.CMD_BROAD_NEXT_LANDLORD: // 被通知显示抢分按钮
				c.updateLandlord(1);
				break;
			case Proxys.CMD_HOST_START_CARDS: // 谁是地主
				byte[] buffer=Proxys.getBuffer(new String[]{
						Integer.toString(Proxys.CMD_START_CARDS),
						c.me.getIp()			
						});
				c.sendCMD(buffer, data[3]);
				break;
			case Proxys.CMD_START_CARDS: // 地主开始出牌
				out("该我出牌了出牌:"+c.me.getDeskId());
				c.showCardButton();
				break;
			case Proxys.CMD_SEND_CURRENTID_CARDS: // 收到当前ID
				out("currentId:"+data[3]);
				c.setCurrentId(Integer.parseInt(data[3]));
				break;
			case Proxys.CMD_SEND_CARDS: // 收到广播来的牌
				int desk1 = (c.ns.gInfo.getCurrentPlayerId() + 2) % 3;
				out("desk1:"+desk1);
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				out("pai:"+c.cardDataList.size());
				c.updateCards(desk1);
				break;
			case Proxys.CMD_HOST_SEND_CARDS: // 有人出牌了
				int desk = -1;
				for (User s : c.users) {
					if (s.getIp().equals(data[2]))
						desk = s.getDeskId();
				}
				out(desk + ":出牌");
				// sendID
				c.sendId(desk);
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				// 将牌分给组内人
				c.sendCards();
				// 下一个人
				if (!(c.Max == 2 && desk == 1))
					c.nextPlayer(desk);
				// Computer出牌
				if (c.Max == 2 && (desk == 1)) {
					// 电脑出牌
					//防止电脑误认问题
					out("电脑出牌");
					if (c.ns.gInfo.playerOutList[1] != null)
					{
						c.ns.old[1].addAll(c.ns.gInfo.playerOutList[1]);
						c.ns.gInfo.playerOutList[1].clear();
					}
					else
						c.ns.gInfo.playerOutList[1]=new ArrayList<Card>();
					for (Card c1 : c.ns.gInfo.playerList[1]) {
						for (CardData card : c.cardDataList) {
							if (card.imageId == c1.imageId) {
								c.ns.gInfo.playerOutList[1].add(c1);
							}
						}
					}
					//////////////////////////
					c.cardDataList.clear();
					c.setCurrentId(2);
					c.ns.gInfo.playerOutList[2] = Comm.getBestAI(
							c.ns.gInfo.playerList[2],
							c.ns.gInfo.getOppo());
					c.ns.gInfo.playerOutList[1].clear();
					if(c.ns.gInfo.getOppo()!=null)
						out("oppo:"+c.ns.gInfo.getOppo().get(0).value);
					else
						out("null");
					c.sendId(2);
					if (c.ns.gInfo.playerOutList[2] == null)
						out("电脑没牌出");
					else {
						out("电脑出的牌:" + c.ns.gInfo.playerOutList[2].size());
						String s = "";
						for (Card cd : c.ns.gInfo.playerOutList[2]) {
							s += cd.value + ",";
							c.cardDataList.add(new CardData(cd.imageId, 2)); 
						}
						out("电脑牌:" + s);
					}
					out("电脑出牌来接收");
					
					
					// 将牌分给组内人
					c.sendCards();
					out("电脑出了牌下一个人出");
					// 下一个人
					c.nextPlayer(2);
				}
				break;
			case Proxys.CMD_HOST_LEAVE_ROOM: // 主机离开房间
				c.broadleaveRoom();
				c.delRoom(c.currentRoom);
				break;
			case Proxys.CMD_LEAVE_ROOM:
				c.init();
				break;
			case Proxys.CMD_DEL_ROOM: // 收到删除房间
				c.r = new Room(data[3],Integer.parseInt(data[4]));
				if (c.isContainsRoom(c.r)) {
					c.removeRoom(c.r);
				}
				c.updateRoomList();
				break;
			
			
		}
		
	}
	public static void out(String s) {
		Log.v("test", s);
	}
	@Override
	public void dispose() {
		stop=true;
		s=null;
	}
}
