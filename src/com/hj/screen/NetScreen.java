package com.hj.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.os.Handler;
import android.os.Message;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hj.net.CardData;
import com.hj.net.Constant;
import com.hj.net.Proxys;
import com.hj.net.User;
import com.hj.player.INetPlayer;
import com.hj.player.LeftPlayer;
import com.hj.player.MidPlayer;
import com.hj.player.RightPlayer;
import com.hj.singlejoker.HjGame;
import com.hj.singlejoker.MainActivity;
import com.hj.singlejoker.ScoreManager;
import com.hj.singlejoker.SoundManager;
import com.hj.tool.Card;
import com.hj.tool.CardType;
import com.hj.tool.Comm;
import com.hj.tool.GameInfo;
 

public class NetScreen implements Screen{

	public Stage stage;
	public MainActivity ma;
	public TextureAtlas atlsa;
	public Constant con=Constant.getCons();
	public GameInfo gInfo;
	INetPlayer player[]=new INetPlayer[3];
	public float width = HjGame.Width;
    public float height = HjGame.Height; 
    Label cardCount[]=new Label[2];
    Image heads[] = new Image[3];
	Image heads2[] = new Image[3];
	BitmapFont bitmapFont;
	LabelStyle style;
	public ArrayList<Card> old[]=new ArrayList[3];
    float _x,_y;
	public NetScreen(MainActivity ma)
	{
		this.ma=ma;
		this.con.ns=this;
		for(int i=0;i<3;i++)
			old[i]=new ArrayList<Card>();
	}
	@Override
	public void show() {
    	//舞台
        stage = new Stage(width, height,true);  
        gInfo=new GameInfo(stage);
        player[0]=new LeftPlayer(gInfo,this) ;
        player[1]=new MidPlayer(gInfo, this);
        player[2]=new RightPlayer(gInfo, this);
        Comm.gInfo=gInfo;
        //初始化
        InitSource();
        //按鈕
        NinePatch n1 = new NinePatch(atlsa.findRegion("btnup"), 14, 14, 14,
        	    14);
        NinePatch n2 = new NinePatch(atlsa.findRegion("btndown"), 14, 14, 14,
        	    14);
        Button.ButtonStyle btnStyle=new Button.ButtonStyle(new Button.ButtonStyle(new NinePatchDrawable(n1),
        		new NinePatchDrawable(n2),new NinePatchDrawable(n1))); 
        NinePatch n11 = new NinePatch(atlsa.findRegion("btn2Up"), 14, 14, 15,
        	    15);
        NinePatch n22 = new NinePatch(atlsa.findRegion("btn2Down"), 14, 14, 15,
        	    15);
        Button.ButtonStyle btnStyle2=new Button.ButtonStyle(new Button.ButtonStyle(new NinePatchDrawable(n11),
        		new NinePatchDrawable(n22),new NinePatchDrawable(n11)) ); 
        //准备按钮
        for(int i=0;i<3;i++)
        {
        	gInfo.readyLabelFinished[i]=new Image(atlsa.findRegion("ready"));  
        	gInfo.readyLabelFinished[i].setVisible(false);
        	stage.addActor(gInfo.readyLabelFinished[i]);
        }
        gInfo.readyLabelFinished[2].setPosition(630, 350);
        gInfo.readyLabelFinished[0].setPosition(100, 350);
        gInfo.readyLabelFinished[1].setPosition(width/2-gInfo.readyLabelFinished[1].getWidth()/2,170 );
       
        //准备
        Button readyBtn=new Button(btnStyle);
        Image readyLabel=new Image(atlsa.findRegion("ready"));
        readyBtn.addActor(readyLabel);
        readyBtn.setWidth(width/7);
        readyLabel.setPosition(readyBtn.getWidth()/2-readyLabel.getWidth()/2, 
        		readyBtn.getHeight()/2-readyLabel.getHeight()/2);
        readyBtn.setPosition(width/2-readyBtn.getWidth()/2, 170);
    	stage.addActor(readyBtn);
    	readyBtn.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				Button btn=(Button)event.getListenerActor();
				gInfo.readyLabelFinished[1].setVisible(true);
				btn.setVisible(false);
				con.getReady();
				super.clicked(event, x, y);
			}
    		
    	});
    	
    	
        //头像
  		Image headsRound[] = new Image[3];
  		for(int i=0;i<3;i++)
  		{
  			headsRound[i]=new Image(atlsa.findRegion("around"));
  			headsRound[i].setHeight(70);
  			headsRound[i].setWidth(70);
  			heads[i]=new Image(atlsa.findRegion("farmHead"));
  			heads[i].setHeight(80);
  			heads[i].setWidth(80);
  			heads2[i]=new Image(atlsa.findRegion("landHead"));
  			heads2[i].setHeight(70);
  			heads2[i].setWidth(70);
  			heads2[i].setVisible(false);
  			stage.addActor(headsRound[i]);
  		}
  		for(int i=0;i<3;i++)
  		{
  			stage.addActor(heads[i]);
  			stage.addActor(heads2[i]);
  		}
  		headsRound[0].setPosition(5, 150);
  		headsRound[1].setPosition(5, 390);
  		headsRound[2].setPosition(730, 390);
  		heads[0].setPosition(5, 390);
  		heads[1].setPosition(5, 150);
  		heads[2].setPosition(730, 390);
  		heads2[0].setPosition(5, 390);
  		heads2[1].setPosition(5, 150);
  		heads2[2].setPosition(730, 390);
        //昵称
  		gInfo.name[0]=new Label("",style);
  		gInfo.name[1]=new Label("",style);
  		gInfo.name[2]=new Label("",style);
  		for(int i=0;i<3;i++)
			stage.addActor(gInfo.name[i]);
  		gInfo.name[1].setPosition(10, 133);
  		gInfo.name[0].setPosition(10, 378);
  		gInfo.name[2].setPosition(735, 378);
  	    //牌数
		cardCount[0]=showText("17");
		cardCount[1]=showText("17");
		cardCount[0].setPosition(21, 303);
		cardCount[1].setPosition(760, 303);
		cardCount[0].setVisible(false);
		cardCount[1].setVisible(false);
		stage.addActor(cardCount[0]);
		stage.addActor(cardCount[1]);
		//分数
        Image label[] ={
        new Image(atlsa.findRegion("no2")),
        new Image(atlsa.findRegion("one")),
        new Image(atlsa.findRegion("two")),
        new Image(atlsa.findRegion("three"))
        };
        //出牌图标
        Image cardLabel[] ={
        		new Image(atlsa.findRegion("no1")),
                new Image(atlsa.findRegion("rechoose")),
                new Image(atlsa.findRegion("tips")),
        		new Image(atlsa.findRegion("send"))
                };
        
       
        //抢分按钮
        for(int i=0;i<4;i++)
        {
        	if(i==0)
        		gInfo.button[i]=new Button(btnStyle2);
        	else
        		gInfo.button[i]=new Button(btnStyle);
        	gInfo.button[i].addActor(label[i]);
        	gInfo.button[i].setWidth(width/6);
        	label[i].setPosition(gInfo.button[i].getWidth()/2-label[i].getWidth()/2, 
        			gInfo.button[i].getHeight()/2-label[i].getHeight()/2);
        	gInfo.button[i].setPosition(100+width/5*i, 170);
        	stage.addActor(gInfo.button[i]);
        	gInfo.button[i].addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Button btn=(Button)event.getListenerActor();
					int i=-1;
					if(btn==gInfo.button[0])
					{
						i=0;
					}else if(btn==gInfo.button[1])
					{
						i=1;
					}else if(btn==gInfo.button[2])
					{
						i=2;
					}else if(btn==gInfo.button[3])
					{
						i=3;
					}
					//存储叫分值
					gInfo.setScore(1,i);
					for(User user:con.users)
					{
						if(user.getIp().equals(con.me.getIp()))
						{
							user.setLandlordScore(i);
							con.me.setLandlordScore(i); 
						}
					}
					//computer叫分
					if(con.Max==2 && con.users.get(1).getIp().equals(con.me.getIp()))
					{
						con.users.get(2).setLandlordScore(Comm.getScore(gInfo.playerList[2]));
						con.out("computer:"+Comm.getScore(gInfo.playerList[2])+"分");
					}
					//隐藏按钮
					ScoreButtonVisible(false);
					con.out("已抢分");
					
					//通知下一个人
					con.broadUser();
					con.finishLandlord();
				}	
			});
        }
        ScoreButtonVisible(false);//先隐藏
        //出牌按钮
        for(int i=0;i<4;i++)
        {
        	if(i==3)
        		gInfo.button[i+4]=new Button(btnStyle2);
        	else
        		gInfo.button[i+4]=new Button(btnStyle);
        	gInfo.button[i+4].addActor(cardLabel[i]);
        	gInfo.button[i+4].setWidth(width/6);
        	cardLabel[i].setPosition(gInfo.button[i+4].getWidth()/2-cardLabel[i].getWidth()/2, 
        			gInfo.button[i+4].getHeight()/2-cardLabel[i].getHeight()/2);
        	gInfo.button[i+4].setPosition(100+width/5*i, 170);
        	stage.addActor(gInfo.button[i+4]);
        	gInfo.button[i+4].setVisible(false);
        	gInfo.button[i+4].addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Button btn=(Button)event.getListenerActor();
					int id=gInfo.getCurrentPlayerId();
					//重选
					if(btn==gInfo.button[5])
					{
						for(Card c:gInfo.playerList[id])
						{
							if(c.clicked==1)
								c.move();
						}
					}
					//提示
					if(btn==gInfo.button[6])
					{
						
						List<Card> tipsCard=Comm.getBestAI(gInfo.playerList[id], gInfo.getOppo());
						for(Card c:gInfo.playerList[id])
						{
							if(c.clicked==1)
								c.move();
						}
						if(gInfo.getOppo()!=null)
						{
							tipsCard=Comm.getTipsCard(gInfo.playerList[id], gInfo.getOppo());
							if(tipsCard!=null)
							{
								for(Card c:tipsCard)
								{
									c.move();
								}
							}else
							{
								//按钮隐藏
								for(int i=4;i<8;i++)
								{
									gInfo.button[i].setVisible(false);
								}
								if(gInfo.playerOutList[id]!=null)
									gInfo.playerOutList[id].clear();
								gInfo.showCancelLabel[1].setVisible(true);
								//广播
								con.sendHostCards();
								gInfo.setCurrentPlayerId((gInfo.getCurrentPlayerId()+1)%3);
							}
							
						}else
						{
							for(Card c:tipsCard)
							{
								c.move();
							}
						}
						
					}
					//出牌
					if(btn==gInfo.button[7])
					{
						gInfo.showCancelLabel[0].setVisible(false);
						gInfo.showCancelLabel[2].setVisible(false);
						if(gInfo.playerOutList[id]!=null)
							gInfo.playerOutList[id].clear();
						else
							gInfo.playerOutList[id]=new ArrayList();
						//把被选中的加入出牌队列,判断是否能出
						for(int i=0,len=gInfo.playerList[id].size();i<len;i++ )
						{
							if(gInfo.playerList[id].get(i).clicked==1)
							{
								gInfo.playerOutList[id].add(gInfo.playerList[id].get(i));
							}
						}
						if((gInfo.playerOutList[id]==null)||(gInfo.playerOutList[id].size()==0)||(Comm.jugdeType(gInfo.playerOutList[id])==CardType.c0))
						{
							//不能出
							con.out("不能出");
						}
						else{
							List<Card> oppo=gInfo.getOppo();
							if(Comm.checkCards(gInfo.playerOutList[id], oppo)==1)
							{
								//按钮隐藏，出牌位置设定
								for(int i=4;i<8;i++)
								{
									gInfo.button[i].setVisible(false);
								}
								//改变游戏顺序
								con.sendHostCards();
								
							}else
							{
								Comm.HjLog("不能出啊");
		
							}
						}
						//如果能,就用已有队列移除
					}
					//不要
					if(btn==gInfo.button[4])
					{
						if(!((gInfo.playerOutList[(con.me.getDeskId()+1)%3]==null||
								gInfo.playerOutList[(con.me.getDeskId()+1)%3].size()==0)
								&&(gInfo.playerOutList[(con.me.getDeskId()+2)%3]==null||
								gInfo.playerOutList[(con.me.getDeskId()+2)%3].size()==0)))
						{
							//按钮隐藏
							for(int i=4;i<8;i++)
							{
								gInfo.button[i].setVisible(false);
							}
							if(gInfo.playerOutList[id]!=null)
								gInfo.playerOutList[id].clear();
							gInfo.showCancelLabel[1].setVisible(true);
							//通知下一个
							con.sendHostCards();
						}
					}
					
				}	
					
			});
        }
        //不要标签
        for(int i=0;i<3;i++)
        {
        	gInfo.showCancelLabel[i]=new Image(atlsa.findRegion("no1"));
        	stage.addActor(gInfo.showCancelLabel[i]);
        	gInfo.showCancelLabel[i].setVisible(false);
        }
        
        gInfo.showCancelLabel[2].setPosition(630, 350);
        gInfo.showCancelLabel[0].setPosition(100, 350);
        gInfo.showCancelLabel[1].setPosition(width/2-gInfo.showCancelLabel[1].getWidth()/2,170 );
        //设置能够接受用户输入
        Gdx.input.setInputProcessor(stage);
        stage.addListener(new ClickListener(){

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				_x=x;
				_y=y;
				
				return super.touchDown(event, x, y, pointer, button);
			}
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
		
				if(gInfo.button[4].isVisible())
				{
					for(Card c:gInfo.playerList[gInfo.CurrentPlayerId])
					{
							c.touched=0;
					}
				}
				super.touchUp(event, x, y, pointer, button);
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				// TODO Auto-generated method stub
				if(gInfo.button[4].isVisible())
				{
					for(Card c:gInfo.playerList[gInfo.CurrentPlayerId])
					{
						if(( (c.getX()>_x && c.getX()<x) ||
								(c.getX()>x-c.getWidth()/2&& c.getX()<_x) 	)
								&&c.touched==0 && y<(c.getY() +c.getHeight()))
						{
							c.touched=1;
							c.move();
						}
					}
				}
				
				super.touchDragged(event, x, y, pointer);
			}	
		});
        //输赢图标，面板
        gInfo.winpage= new Image(atlsa.findRegion("page"));
        gInfo.winpage.setVisible(false);
        gInfo.winpage.setPosition(width/2-gInfo.winpage.getWidth()/2-3, height-220);
        stage.addActor(gInfo.winpage);
        gInfo.winLabel[0]=new Image(atlsa.findRegion("landwin"));
        gInfo.winLabel[1]=new Image(atlsa.findRegion("farmwin"));
        gInfo.winLabel[0].setVisible(false);
        gInfo.winLabel[1].setVisible(false);
        gInfo.winLabel[0].setPosition(330, 430);
        stage.addActor(gInfo.winLabel[0]);
        gInfo.winLabel[1].setPosition(330, 430);
        stage.addActor(gInfo.winLabel[1]);
        gInfo.labelpage[0]=new Image(atlsa.findRegion("farm"));
        gInfo.labelpage[1]=new Image(atlsa.findRegion("land"));
        //返回继续按钮
        Image labelbtn[] ={
                new Image(atlsa.findRegion("continue")),
                new Image(atlsa.findRegion("back"))};
        for(int i=0;i<2;i++)
        {
        	gInfo.labelpage[i].setPosition(270+150*i, 350);
        	stage.addActor(gInfo.labelpage[i]);
        	gInfo.labelpage[i].setVisible(false);
        	gInfo.winScore[i]=new Label("40", style);
        	gInfo.winScore[i].setPosition(355+150*i, 355);
        	stage.addActor(gInfo.winScore[i]);
        	gInfo.winScore[i].setVisible(false);
        	gInfo.winBtn[i]=new Button(btnStyle2);
        	gInfo.winBtn[i].addActor(labelbtn[i]);
        	gInfo.winBtn[i].setWidth(width/7);
        	gInfo.winBtn[i].setHeight(60);
        	labelbtn[i].setPosition(gInfo.winBtn[i].getWidth()/2-labelbtn[i].getWidth()/2, 
        			gInfo.winBtn[i].getHeight()/2-labelbtn[i].getHeight()/2);
        	gInfo.winBtn[i].setPosition(250+170*i, 260);
        	gInfo.winBtn[i].setVisible(false);
         	stage.addActor(gInfo.winBtn[i]);
         	gInfo.winBtn[i].addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Button btn=(Button)event.getListenerActor();
					if(btn==gInfo.winBtn[0])
					{
						con.leaveRoom();
						//ma.hg.setScreen(ma.rs);
					}
					if(btn==gInfo.winBtn[1])
					{
						con.leaveRoom();
						ma.hg.setScreen(ma.us);
					}
				}
         	});
        }
        
	}
	//新建字符串标签
	public Label showText(String s)
	{
		BitmapFont bf=new BitmapFont();
		bf.setColor(Color.WHITE);
		return new Label(s,new LabelStyle(bf, bf.getColor()) );
	}
	//隐藏抢分按钮
	public void ScoreButtonVisible(boolean b)
	{
		int i=0;
		for(;i<4;i++)
			gInfo.button[i].setVisible(b);
	}
	
	@Override
	public void dispose() {
	
	    stage.dispose();
	    atlsa.dispose();
	}

	@Override
	public void hide() {
	
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void render(float arg0) {
	
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());  
        stage.draw(); 
        if(con.backTORommScreen)
        {
        	con.backTORommScreen=false;
        	ma.hg.setScreen(ma.rs);
        	con.backTORommScreen=false;
        }
  
	}

	@Override
	public void resize(int arg0, int arg1) {
	
		
	}

	@Override
	public void resume() {
		
		
	}
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what)
			{
				case Proxys.MES_UPDATE_User://更新用户列表
					updateGame();
					break;
				case Proxys.MES_UPDATE_BEGINCARD: //发牌
					updateOpenCard();
					break;
				case Proxys.MES_UPDATE_LANDLORD: //显示抢分按钮
					updateOpenLanlord((Integer)msg.obj);
					break;
				case Proxys.MES_UPDATE_LANDLORDHEAD:// 显示地主
					updateLandLord();
					break;
				case Proxys.MES_SHOW_CARDBUTTON: //显示出牌按钮
					showCardButton();
					break;
				case Proxys.MES_SEND_CARDS:// 收到出牌
					showCards((Integer)msg.obj);
					break;
				case Proxys.MES_FLUSH_CARDS://刷新牌
					flush((List<Card>)msg.obj);
					break;

			}
		}
	};
	public void flush(List<Card> list)
	{
		if(list!=null)
		{
			for(Card c:list)
			{
				c.setVisible(false);
			}
		}else
		{
			SoundManager.PlayPass();
		}
		if(con.me.getDeskId()==gInfo.getCurrentPlayerId())
			gInfo.showCancelLabel[1].setVisible(false);
		
	}
	public void showCards(int deskId)
	{
		if(old[deskId].size()>0)
		{
			for(Card o:old[deskId])
			{
				o.setVisible(false);
			}
			
		}else{
			
		}
		old[deskId].clear();
		if(gInfo.playerOutList[deskId]!=null)
			old[deskId].addAll(gInfo.playerOutList[deskId]);
		if(gInfo.playerOutList[deskId]!=null)
		{
			for(Card c:gInfo.playerOutList[deskId])
			{
				c.setVisible(false);
			}
		}
		if (gInfo.playerOutList[deskId] != null)
			gInfo.playerOutList[deskId].clear();
		for (Card c1 : gInfo.playerList[deskId]) {
			for (CardData card : con.cardDataList) {
				if (card.imageId == c1.imageId) {
					gInfo.playerOutList[deskId].add(c1);
				}
			}
		}
		if (gInfo.playerOutList[deskId] != null)
			gInfo.playerList[deskId]
					.removeAll(gInfo.playerOutList[deskId]);
		int playerID=getPlayerId(con.me.getDeskId(),deskId);
		con.out("id:"+playerID+":"+con.me.getDeskId()+":"+deskId);
		if(gInfo.playerOutList[deskId]==null || gInfo.playerOutList[deskId].size()==0)
		{
			//不要
			gInfo.showCancelLabel[playerID].setVisible(true);
			con.out("不要:");
		}else
		{
			gInfo.showCancelLabel[playerID].setVisible(false);
			if(gInfo.playerList[deskId].size()<3)
				SoundManager.PlayRemain(gInfo.playerList[deskId].size());
			Comm.rePosition(gInfo.playerList[deskId], playerID);
			Comm.setOutPosition(gInfo.playerOutList[deskId], playerID);
		}
		//改变牌数
		ChangeCardCount();
		//判断输赢
		gInfo.isWin();
		
	}
	public void showCardButton()
	{
		gInfo.setCurrentPlayerId(con.me.getDeskId());
		//隐藏之前出的牌
		if(gInfo.playerOutList[con.me.getDeskId()]!=null)
		{
			for(Card c:gInfo.playerOutList[con.me.getDeskId()])
			{
				c.setVisible(false);
			}
			gInfo.playerOutList[con.me.getDeskId()].clear();
		}
		gInfo.showCancelLabel[1].setVisible(false);
		//显示按钮
		for(int i=4;i<8;i++)
		{
			gInfo.button[i].setVisible(true);
		}
	}
	public void updateLandLord()
	{
		//显示地主头像label
		//NetManager.out("地主头像ID:"+getPlayerId(net.me.getDeskId(),gInfo.LandlordId));
		int id=getPlayerId(con.me.getDeskId(),gInfo.LandlordId);
		heads[id].setVisible(false);
		heads2[id].setVisible(true);
		//修正地主牌
		for(Card cards:gInfo.playerList[3])
				cards.setFront();
	}
	public void updateOpenLanlord(int b)
	{
		if(b==1)
			ScoreButtonVisible(true);
		else
			ScoreButtonVisible(false);
	}
	public void updateOpenCard()
	{
		//Sound
		SoundManager.PlayStart();
		for(int i=0;i<3;i++)
		{
			player[getPlayerId(con.me.getDeskId(), i)].showOpenCard(i);
		}
		int count=0;
		//地主牌
		for(Card cards:gInfo.playerList[3])
		{
			cards.setZIndex(count);
			cards.setPosition(width / 2 - cards.mTextureRegion.getRegionWidth() / 2, height
					/ 2 - cards.mTextureRegion.getRegionHeight() / 2);
			cards.addAction(Actions.moveTo(width/2-65+(count++)*40,350, 1f));
			stage.addActor(cards);
			cards.setScale(0.5f);
			cards.setBack();
		}
		ChangeCardCount();
	}
	public void updateGame()
	{
		for(int i=0;i<3;i++)
		{
			player[i].showName("");
			if(gInfo.showScoreLabel[i]!=null)
				gInfo.showScoreLabel[i].setVisible(false);
		}
		for(int i=0;i<con.users.size();i++)
		{
			//名称
			player[getPlayerId(con.me.getDeskId(),con.users.get(i).getDeskId())].showName(con.users.get(i).getName());
			//准备
			player[getPlayerId(con.me.getDeskId(),con.users.get(i).getDeskId())].showReady(con.users.get(i).getIsReady());
			//抢分
			player[getPlayerId(con.me.getDeskId(),con.users.get(i).getDeskId())].showLandlordScore(con.users.get(i).getLandlordScore());
			//判断底分
			if(con.users.get(i).getLandlordScore()>ScoreManager.baseScore)
			{
				ScoreManager.baseScore=con.users.get(i).getLandlordScore();
				gInfo.bscores.setText(ScoreManager.baseScore+"");
			}
		
		}
		
	}
	public int getPlayerId(int myId,int deskId)
	{
		if(myId==deskId)
			return 1;
		else if((myId+1)%3==deskId)
			return 2;
		else 
			return 0;
	}
	//改变牌数
	public void ChangeCardCount()
	{
			cardCount[0].setVisible(true);
			cardCount[0].setText(gInfo.playerList[(con.me.getDeskId()+2)%3].size()+"");
			cardCount[1].setVisible(true);
			cardCount[1].setText(gInfo.playerList[(con.me.getDeskId()+1)%3].size()+"");
	}
	//初始化
	public void InitSource()
	{
		//资源
        AssetManager m=HjGame.getManager();
        //背景
        atlsa=m.get("data/resource.txt",TextureAtlas.class);
        Image imageBg = new Image(m.get("data/bg.txt",TextureAtlas.class).findRegion("bgMain"));  
        imageBg.setFillParent(true);
        stage.addActor(imageBg);  
		// GameUI
        //字体
        bitmapFont = new BitmapFont(Gdx.files.internal("data/word.fnt"), Gdx.files.internal("data/word.png"), false);
        style = new LabelStyle(bitmapFont, bitmapFont.getColor()); 
        //TopBar
        Image top=new Image(atlsa.findRegion("top"));
        top.setPosition(width/2-top.getWidth()/2,height-top.getHeight()+6 );
        stage.addActor(top); 
        //底分倍数
        Image bScore=new Image(atlsa.findRegion("bScore"));
        bScore.setPosition(top.getX()+75, top.getY()+15);
        Image times=new Image(atlsa.findRegion("times"));
        times.setPosition(width/2, top.getY()+15);
        stage.addActor(bScore);
        stage.addActor(times);
        ScoreManager.baseScore=1;
        ScoreManager.baseTimes=1;
        gInfo.bscores=new Label(ScoreManager.baseScore+"",style);
        gInfo.btimes=new Label(ScoreManager.baseTimes+"",style);
        stage.addActor(gInfo.bscores);
        stage.addActor(gInfo.btimes);
        gInfo.bscores.setPosition(bScore.getX()+84, bScore.getY());
        gInfo.btimes.setPosition(times.getX()+84, times.getY());
        //homeButton
        Button home= new ImageButton(new TextureRegionDrawable(atlsa.findRegion("home2")),new TextureRegionDrawable(atlsa.findRegion("home1")));
        stage.addActor(home);
        home.setPosition(top.getX()+30, top.getY()+10);
        home.addListener(new ClickListener()
        {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				con.leaveRoom();
				super.clicked(event, x, y);
			}
			
        });
		//settingButton
        Button setting= new ImageButton(new TextureRegionDrawable(atlsa.findRegion("set1")),new TextureRegionDrawable(atlsa.findRegion("set")));
        stage.addActor(setting);
        setting.setPosition(top.getX()+top.getWidth()-67, top.getY()+12);
        setting.addListener(new ClickListener()
        {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				con.leaveRoom();
				ma.hg.setScreen(ma.ss);
				super.clicked(event, x, y);
			}
			
        });
	}
	
	
}
