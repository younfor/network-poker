package com.hj.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.util.Log;

public class Proxys {

	    // 协议命令
		public static String head="head",div=",",end="end";
		public static final int CMD_HOST_REQUEST_ROOM = 10;
		public static final int CMD_REPLY_ROOM = 11;
		public static final int CMD_CREATE_ROOM = 12;
		public static final int MES_UPDATE_ROOM = 13;
		public static final int MES_UPDATE_User = 14;
		public static final int CMD_HOST_JOIN_ROOM = 15;
		public static final int CMD_BROAD_USERLIST = 16;
		public static final int CMD_UPDATE_ROOM = 17;
		public static final int CMD_HOST_LEAVE_ROOM = 18;
		public static final int CMD_LEAVE_ROOM = 19;
		public static final int CMD_DEL_ROOM = 20;
		public static final int CMD_HOST_READY_ROOM = 21;
		public static final int CMD_BEGIN_CARDS = 22;
		public static final int MES_UPDATE_BEGINCARD = 23;
		public static final int MES_UPDATE_LANDLORD = 24;
		public static final int CMD_HOST_FINISH_LANDLORD = 25;
		public static final int CMD_BROAD_NEXT_LANDLORD = 26;
		public static final int CMD_BEGIN_LANDLORD_CARDS = 27;
		public static final int CMD_HOST_START_CARDS = 28;
		public static final int CMD_START_CARDS = 29;
		public static final int MES_SHOW_CARDBUTTON = 30;
		public static final int MES_UPDATE_LANDLORDHEAD = 31;
		public static final int CMD_HOST_SEND_CARDS = 32;
		public static final int CMD_SEND_CARDS = 33;
		public static final int MES_SEND_CARDS = 34;
		public static final int CMD_SEND_CURRENTID_CARDS = 35;
		public static final int MES_FLUSH_CARDS = 36;
		public static final int CMD_HOST_SEND_CARDS_COMPUTER = 37,MES_HIDE_COMPUTERCARDS=38;
		public static final int MES_CHANGE_SCREEN = 39;
		public static final int PORT_RECEIVE = 12425;// 接收端口
		
		public static byte[] getBuffer(String s[])
		{
			String buffer=head;;
			for(int i=0;i<s.length;i++)
			{
				buffer+=div+s[i];
			}
			buffer+=div+end;
			return buffer.getBytes();  
		}
		public static byte[] getBufferUser(List<User> list)
		{
			String buffer=head;
			buffer+=div+Integer.toString(Proxys.CMD_BROAD_USERLIST);
			buffer+=div+Constant.getCons().me.getIp();
			for(User u:list)
			{
				buffer+=div+u.getName();
				buffer+=div+u.getIp();
				buffer+=div+u.getDeskId();
				buffer+=div+u.getRoomId();
				buffer+=div+u.getIsReady();
				buffer+=div+u.getLandlordScore();
			}
			buffer+=div+end;
			return buffer.getBytes();  
		}
		public static byte[] getBufferCard(List<CardData> list,int cmd)
		{
			String buffer=head;
			buffer+=div+Integer.toString(cmd);
			buffer+=div+Constant.getCons().me.getIp();
			for(CardData cd:list)
			{
				buffer+=div+cd.getDeskId()+":"+cd.getImageId();
			}
			buffer+=div+end;
			return buffer.getBytes();  
		}
		// 得到广播ip, 192.168.0.255之类的格式
		public static String getBroadCastIP() {
			String ip = getLocalHostIp().substring(0,
					getLocalHostIp().lastIndexOf(".") + 1)
					+ "255";
			return ip;
		}

		// 获取本机IP
		public static String getLocalHostIp() {
			String ipaddress = "";
			try {
				Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces();
				// 遍历所用的网络接口
				while (en.hasMoreElements()) {
					NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
					Enumeration<InetAddress> inet = nif.getInetAddresses();
					// 遍历每一个接口绑定的所有ip
					while (inet.hasMoreElements()) {
						InetAddress ip = inet.nextElement();
						if (!ip.isLoopbackAddress()
								&& InetAddressUtils.isIPv4Address(ip
										.getHostAddress())) {
							return ipaddress = ip.getHostAddress();
						}
					}

				}
			} catch (SocketException e) {
				System.out.print("获取IP失败");
				e.printStackTrace();
			}
			return ipaddress;

		}
	public static void out(String s) {
		Log.v("test", s);
	}
}
