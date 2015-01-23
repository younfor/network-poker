package com.hj.screen;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.text.style.LineHeightSpan.WithDensity;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

import com.hj.net.User;
import com.hj.player.Computer;
import com.hj.player.Human;
import com.hj.player.IPlayer;
import com.hj.singlejoker.HjGame;
import com.hj.singlejoker.MainActivity;
import com.hj.singlejoker.ScoreManager;
import com.hj.singlejoker.SoundManager;
import com.hj.tool.Card;
import com.hj.tool.CardType;
import com.hj.tool.Comm;
import com.hj.tool.GameInfo;


public class MainScreen implements Screen {

	public MainActivity ma;
	private Stage stage;
	Label cardCount[]=new Label[2];
	TextureAtlas atlsa;// 自定义字体纹理
	Card cards[] = new Card[56];// 1-54 55是背面
	Image showScoreLabel[]=new Image[3];//显示抢分分数
	BitmapFont bitmapFont;
	LabelStyle style;
	
	Image heads[] = new Image[3];
	Image heads2[] = new Image[3];
	
	GameInfo gInfo;
	Human human;
	Computer computer;
	// 开始
	float width = HjGame.Width;
	float height = HjGame.Height;
	float mTime=0,_x,_y;

	public MainScreen(MainActivity ma) {
		this.ma = ma;
		ma.con.me=new User();
		ma.con.me.setDeskId(1);
	}
	//资源
	@Override
	public void show() {
		// 舞台
		stage = new Stage(width, height,true); 
		gInfo=new GameInfo(stage);
		human=new Human(gInfo);
		computer=new Computer(gInfo);
		Comm.gInfo=gInfo;
		//资源
        AssetManager m=HjGame.getManager();
        //背景
        atlsa=m.get("data/resource.txt",TextureAtlas.class);
        Image imageBg = new Image(m.get("data/bg.txt",TextureAtlas.class).findRegion("bgMain"));  
        imageBg.setFillParent(true);
        stage.addActor(imageBg);  
        //字体
        bitmapFont = new BitmapFont(Gdx.files.internal("data/word.fnt"), Gdx.files.internal("data/word.png"), false);
        style = new LabelStyle(bitmapFont, bitmapFont.getColor()); 
		// GameUI
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
				ma.hg.setScreen(ma.us);
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
				ma.hg.setScreen(ma.ss);
				super.clicked(event, x, y);
			}
			
        });
        init();

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
		
				for(Card c:gInfo.playerList[1])
				{
						c.touched=0;
				}
				super.touchUp(event, x, y, pointer, button);
			}
			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				// TODO Auto-generated method stub
				for(Card c:gInfo.playerList[1])
				{
					if(( (c.getX()>_x && c.getX()<x) ||
							(c.getX()>x-c.getWidth()/2&& c.getX()<_x) 	)
							&&c.touched==0 && y<(c.getY() +c.getHeight()))
					{
						c.touched=1;
						if(gInfo.getGameState()==2)
						{
							c.move();
						}
					}
				}
				super.touchDragged(event, x, y, pointer);
			}	
		});
	}

	@Override
	public void dispose() {
		
		Comm.HjLog("dispose");
	}

	@Override
	public void pause() {
		Comm.HjLog("pause");
	}
	
	@Override
	public void render(float e) {
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		//开始
		ChangeState(1, 3);
		//游戏循环
		switch(gInfo.getGameState())
		{
			case 1://抢地主
				switch(gInfo.getCurrentPlayerId())
				{
					case -1:
						gInfo.FirstLandLordId=Comm.getFirstScoreId();
						gInfo.setCurrentPlayerId(gInfo.FirstLandLordId);
						Comm.HjLog("初始化抢分"+gInfo.FirstLandLordId);
						break;
					case 0:
						gInfo.PlayerActive[0]=true;
						gInfo.setScore(0, Comm.getScore(gInfo.playerList[0]));
						//显示分数
						switch(Comm.getScore(gInfo.playerList[0]))
						{
							case 0:showScoreLabel[0]=new Image(atlsa.findRegion("no1"));
								break;
							case 1:showScoreLabel[0]=new Image(atlsa.findRegion("one"));
								break;
							case 2:showScoreLabel[0]=new Image(atlsa.findRegion("two"));
								break;
							case 3:showScoreLabel[0]=new Image(atlsa.findRegion("three"));
								break;
						}
						showScoreLabel[0].setPosition(100, 350);
						stage.addActor(showScoreLabel[0]);
						gInfo.setCurrentPlayerId((gInfo.getCurrentPlayerId()+1)%3);
						gInfo.checkAllActive();
						break; 
					case 1:
						ScoreButtonVisible(true);
						gInfo.PlayerActive[1]=true;
						break;
					case 2:
						gInfo.PlayerActive[2]=true;
						gInfo.setScore(2, Comm.getScore(gInfo.playerList[2]));
						//显示分数
						switch(Comm.getScore(gInfo.playerList[2]))
						{
							case 0:showScoreLabel[2]=new Image(atlsa.findRegion("no1"));
								break;
							case 1:showScoreLabel[2]=new Image(atlsa.findRegion("one"));
								break;
							case 2:showScoreLabel[2]=new Image(atlsa.findRegion("two"));
								break;
							case 3:showScoreLabel[2]=new Image(atlsa.findRegion("three"));
								break;
						}
						showScoreLabel[2].setPosition(640, 350);
						stage.addActor(showScoreLabel[2]);
						gInfo.setCurrentPlayerId((gInfo.getCurrentPlayerId()+1)%3);
						gInfo.checkAllActive();
						break;
					case 3://切换状态
						showScoreLabelVisible(false,3);//隐藏分数显示标志
						break;
				}
				
				break;
			case 2://开始游戏
				gInfo.delay+=Gdx.graphics.getDeltaTime();
				gInfo.isWin();
				switch(gInfo.getCurrentPlayerId())
				{
					case 0:
						if(gInfo.delay>1)
						{
							computer.sendCard();
							ChangeCardCount(0);
							gInfo.delay=0;
						}
						
						break;
					case 1:
						while(!gInfo.button[6].isVisible())
						{
							human.sendCard();
						}
						break;
					case 2:
						if(gInfo.delay>1)
						{
							computer.sendCard();
							ChangeCardCount(2);
							gInfo.delay=0;
						}
						break;
				}
				break;
			case 3: break;
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void resume() {
		Comm.HjLog("resume ");
	}

	@Override
	public void hide() {
		Comm.HjLog("hide");
	}

	// 初始化游戏数据
	public void init() {
		SoundManager.PlayStart();
		int len = 54;
		for (int i = 1; i <= len; i++) {
			cards[i] = new Card(i, atlsa);
			cards[i].setPosition(width / 2 - cards[i].mTextureRegion.getRegionWidth() / 2, height
					/ 2 - cards[i].mTextureRegion.getRegionHeight() / 2);
			stage.addActor(cards[i]);
		}
		//洗牌
		for(int i=0;i<100;i++){
			Random random=new Random();
			int a=random.nextInt(54)+1;
			int b=random.nextInt(54)+1;
			Card k=cards[a];
			cards[a]=cards[b];
			cards[b]=k;
		}
		//发牌
		for (int i = 1; i <= 51; i++)
		{
			cards[i].setZIndex(i);
			if(i%3==1)
			{
				cards[i].addAction(Actions.moveTo(50 + i * 13, 20, 1f));
				gInfo.playerList[1].add(cards[i]);
				
				
			}
			else if(i%3==2)
			{
				cards[i].addAction(Actions.moveTo(740,303,1f));
				gInfo.playerList[2].add(cards[i]);
				cards[i].setScale(0.5f);
				cards[i].setBack();
			}
			else if(i%3==0)
			{
				cards[i].addAction(Actions.moveTo(2,303, 1f));
				gInfo.playerList[0].add(cards[i]);
				cards[i].setScale(0.5f);
				cards[i].setBack();
			}
		}
		//地主牌
		for(int i=52;i<=54;i++)
		{
			cards[i].addAction(Actions.moveTo(width/2-65+(i-52)*40,350, 2f));
			cards[i].setScale(0.5f);
			cards[i].setBack();
		}
		//排序
		for(int i=0;i<3;i++)
		{
			Comm.setOrder(gInfo.playerList[i]);
		}
		Comm.rePosition(gInfo.playerList[1],1);
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
		Label name[]={
				new Label("王者",style),
				new Label(ScoreManager.getName(ScoreManager.getScore()),style),
				new Label("王者",style)
		};
		//牌数
		cardCount[0]=showText("17");
		cardCount[1]=showText("17");
		cardCount[0].setPosition(21, 303);
		cardCount[1].setPosition(760, 303);
		stage.addActor(cardCount[0]);
		stage.addActor(cardCount[1]);
		for(int i=0;i<3;i++)
			stage.addActor(name[i]);
		name[1].setPosition(10, 125);
		name[0].setPosition(10, 365);
		name[2].setPosition(743, 365);
		
        NinePatch n1 = new NinePatch(atlsa.findRegion("btnup"), 14, 14, 15,
        	    15);
        NinePatch n2 = new NinePatch(atlsa.findRegion("btndown"), 14, 14, 15,
        	    15);
        NinePatch n11 = new NinePatch(atlsa.findRegion("btn2Up"), 14, 14, 15,
        	    15);
        NinePatch n22 = new NinePatch(atlsa.findRegion("btn2Down"), 14, 14, 15,
        	    15);
        Button.ButtonStyle btnStyle=new Button.ButtonStyle(new Button.ButtonStyle(new NinePatchDrawable(n1),
        		new NinePatchDrawable(n2),new NinePatchDrawable(n1))); 
        Button.ButtonStyle btnStyle2=new Button.ButtonStyle(new Button.ButtonStyle(new NinePatchDrawable(n11),
        		new NinePatchDrawable(n22),new NinePatchDrawable(n11))); 
        //分数
        Image label[] ={
        new Image(atlsa.findRegion("no2")),
        new Image(atlsa.findRegion("one")),
        new Image(atlsa.findRegion("two")),
        new Image(atlsa.findRegion("three"))
        };
        
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
        Image labelbtn[] ={
                new Image(atlsa.findRegion("continue")),
                new Image(atlsa.findRegion("back"))};
        for(int i=0;i<2;i++)
        {
        	gInfo.labelpage[i].setPosition(270+150*i, 350);
        	stage.addActor(gInfo.labelpage[i]);
        	gInfo.winScore[i]=new Label("40", style);
        	gInfo.winScore[i].setPosition(355+150*i, 355);
        	stage.addActor(gInfo.winScore[i]);
        	gInfo.winScore[i].setVisible(false);
        	gInfo.labelpage[i].setVisible(false);
        	
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
						ma.hg.setScreen(ma.ms);
					}
					if(btn==gInfo.winBtn[1])
					{
						ma.hg.setScreen(ma.us);
					}
				}
         	});
        }
        
       
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
					//显示分数
					switch(i)
					{
						case 0:showScoreLabel[1]=new Image(atlsa.findRegion("no1"));
							break;
						case 1:showScoreLabel[1]=new Image(atlsa.findRegion("one"));
							break;
						case 2:showScoreLabel[1]=new Image(atlsa.findRegion("two"));
							break;
						case 3:showScoreLabel[1]=new Image(atlsa.findRegion("three"));
							break;
					}
					showScoreLabel[1].setPosition(width/2-showScoreLabel[1].getWidth()/2,170 );
					stage.addActor(showScoreLabel[1]);
					//隐藏按钮
					ScoreButtonVisible(false);
					gInfo.setCurrentPlayerId((gInfo.getCurrentPlayerId()+1)%3);
					gInfo.checkAllActive();
				}	
			});
        }
        ScoreButtonVisible(false);//先隐藏
        //出牌图标
        Image cardLabel[] ={
        		new Image(atlsa.findRegion("no1")),
                new Image(atlsa.findRegion("rechoose")),
                new Image(atlsa.findRegion("tips")),
        		new Image(atlsa.findRegion("send"))
                };
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
					//重选
					if(btn==gInfo.button[5])
					{
						for(Card c:gInfo.playerList[1])
						{
							if(c.clicked==1)
								c.move();
						}
					}
					//提示
					if(btn==gInfo.button[6])
					{
						List<Card> tipsCard=Comm.getBestAI(gInfo.playerList[1], gInfo.getOppo());
						for(Card c:gInfo.playerList[1])
						{
							if(c.clicked==1)
								c.move();
						}
						if(gInfo.getOppo()!=null)
						{
							tipsCard=Comm.getTipsCard(gInfo.playerList[1], gInfo.getOppo());
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
								if(gInfo.playerOutList[1]!=null)
									gInfo.playerOutList[1].clear();
								gInfo.showCancelLabel[1].setVisible(true);
								//改变游戏顺序
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
						Comm.tipOffset=0;
						gInfo.showCancelLabel[0].setVisible(false);
						gInfo.showCancelLabel[2].setVisible(false);
						if(gInfo.playerOutList[1]!=null)
							gInfo.playerOutList[1].clear();
						else
							gInfo.playerOutList[1]=new ArrayList();
						//把被选中的加入出牌队列,判断是否能出
						for(int i=0,len=gInfo.playerList[1].size();i<len;i++ )
						{
							if(gInfo.playerList[1].get(i).clicked==1)
							{
								gInfo.playerOutList[1].add(gInfo.playerList[1].get(i));
								Comm.HjLog("不能出:"+gInfo.playerList[1].get(i).value);
							}
						}
						if((gInfo.playerOutList[1]==null)||(gInfo.playerOutList[1].size()==0)||(Comm.jugdeType(gInfo.playerOutList[1])==CardType.c0))
						{
							Comm.HjLog("不能出:type:"+Comm.jugdeType(gInfo.playerOutList[1]));
							for(Card c:gInfo.playerOutList[1])
								Comm.HjLog(c.value+"");
							
			
						}
						else{
							List<Card> oppo=gInfo.getOppo();
							if(Comm.checkCards(gInfo.playerOutList[1], oppo)==1)
							{
								gInfo.playerList[1].removeAll(gInfo.playerOutList[1]);
								
								Comm.HjLog("能出,还剩:"+gInfo.playerList[1].size());
								//按钮隐藏，出牌位置设定
								for(int i=4;i<8;i++)
								{
									gInfo.button[i].setVisible(false);
								}
								Comm.setOutPosition(gInfo.playerOutList[1], 1);
								Comm.rePosition(gInfo.playerList[1],1);
								//改变游戏顺序
								gInfo.setCurrentPlayerId((gInfo.getCurrentPlayerId()+1)%3);
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
						if(gInfo.getOppo()!=null)
						{
							SoundManager.PlayPass();
							//按钮隐藏
							for(int i=4;i<8;i++)
							{
								gInfo.button[i].setVisible(false);
							}
							if(gInfo.playerOutList[1]!=null)
								gInfo.playerOutList[1].clear();
							gInfo.showCancelLabel[1].setVisible(true);
							//改变游戏顺序
							gInfo.setCurrentPlayerId((gInfo.getCurrentPlayerId()+1)%3);
						}
					}
					gInfo.delay=0;
					
				}	
			});
        }
        //不要按钮
        for(int i=0;i<3;i++)
        {
        	gInfo.showCancelLabel[i]=new Image(atlsa.findRegion("no1"));
        	stage.addActor(gInfo.showCancelLabel[i]);
        	gInfo.showCancelLabel[i].setVisible(false);
        }
        
        gInfo.showCancelLabel[2].setPosition(630, 350);
        gInfo.showCancelLabel[0].setPosition(100, 350);
        gInfo.showCancelLabel[1].setPosition(width/2-gInfo.showCancelLabel[1].getWidth()/2,170 );
	}
	//新建字符串标签
	public Label showText(String s)
	{
		BitmapFont bf=new BitmapFont();
		bf.setColor(Color.WHITE);
		return new Label(s,new LabelStyle(bf, bf.getColor()) );
	}
	//新建字符串标签
	public Label showTextYellow(String s)
	{
		BitmapFont bf=new BitmapFont();
		bf.setColor(Color.YELLOW);
		return new Label(s,new LabelStyle(bf, bf.getColor()) );
	}
	//隐藏抢分按钮
	public void ScoreButtonVisible(boolean b)
	{
		int i=0;
		for(;i<4;i++)
		{
			gInfo.button[i].setVisible(b);
			gInfo.button[i].setDisabled(true);
		}
	}
	//隐藏喊分标签
	public void showScoreLabelVisible(boolean b,float delay)
	{
		if(showScoreLabel[0].isVisible()!=b)
		{
			if(mTime<delay)
			{
				mTime+=Gdx.graphics.getDeltaTime();
			}else
			{
				for(int i=0;i<3;i++)
					showScoreLabel[i].setVisible(b);
				//将地主牌给地主，并给地主带上王冠，重新排序
				gInfo.LandlordId=Comm.getLandOwnerId(gInfo.Score,gInfo.FirstLandLordId );
				gInfo.setCurrentPlayerId(gInfo.LandlordId);
				//显示地主头像label
				Comm.HjLog("地主是:"+gInfo.LandlordId);
				heads[gInfo.LandlordId].setVisible(false);
				heads2[gInfo.LandlordId].setVisible(true);
				for(int i=0;i<3;i++)
				{
					Card c=new Card(cards[i+52].imageId, atlsa);
					if(gInfo.LandlordId!=1)
						c.setScale(0.5f);
					if(gInfo.LandlordId!=1)
						c.setBack();
					gInfo.playerList[gInfo.LandlordId].add(c);
					cards[i+52].setFront();
					stage.addActor(c);
				}
				Comm.setOrder(gInfo.playerList[gInfo.LandlordId]);
				Comm.rePosition(gInfo.playerList[gInfo.LandlordId],gInfo.LandlordId);
				for(Card c:gInfo.playerList[1])
				{
					c.addListener(new ClickListener(){
						@Override
						public boolean touchDown(InputEvent event, float x, float y,
								int pointer, int button) {
							if(gInfo.getGameState()==2)
							{
								Card c=(Card)event.getListenerActor();
								c.move();
								c.touched=1;
								Log.v("test", "touch"+c.imageId);
							}
							
							return super.touchDown(event, x, y, pointer, button);
						}		
					});
				}
				
				gInfo.setGameState(2);
				mTime=0;
			}	
		}
		
	}
	//改变牌数
	public void ChangeCardCount(int id)
	{
		if(id==0)
		{
			cardCount[0].setText(gInfo.playerList[0].size()+"");
			
		}
		else if(id==2)
		{
			cardCount[1].setText(gInfo.playerList[2].size()+"");
		}
			
	}
	//改变游戏状态
	public void ChangeState(int newstate,float delay)
	{
		if(gInfo.getGameState()<newstate)
		{
			if(mTime<delay)
			{
				mTime+=Gdx.graphics.getDeltaTime();
			}else
			{
				gInfo.setGameState(newstate);
				mTime=0;
			}
		}
	}

	


}