package com.hj.screen;


import javax.microedition.khronos.opengles.GL10;

import android.net.Proxy;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.hj.net.Constant;
import com.hj.net.Proxys;
import com.hj.net.Server;
import com.hj.net.User;
import com.hj.singlejoker.HjGame;
import com.hj.singlejoker.MainActivity;
import com.hj.singlejoker.ScoreManager;

public class RommScreen implements Screen{

	Stage stage;
	BitmapFont bitmapFont;
	LabelStyle style;
	Button button[]=new Button[8];//八个桌子
	Label label[]=new Label[8];//桌名
	Constant con;
	public MainActivity ma;
	TextureAtlas atlsa;
	Server s;
	public RommScreen(MainActivity ma)
	{
		this.ma=ma;
		this.con=Constant.getCons();
		this.con.rs=this;
	}
	@Override
	public void show() {
		con.backTORommScreen=false;
		//开始
		float width = HjGame.Width;
        float height = HjGame.Height; 
    	//舞台
        stage = new Stage(width, height,true);  
        //资源
        AssetManager m=HjGame.getManager();
        //背景
        atlsa=m.get("data/resource.txt",TextureAtlas.class);
        Image imageBg = new Image(m.get("data/bg.txt",TextureAtlas.class).findRegion("bgUi"));  
        imageBg.setFillParent(true);
        stage.addActor(imageBg);  
        // GameUI
        //TopBar
        Image top=new Image(atlsa.findRegion("top"));
        top.setPosition(width/2-top.getWidth()/2,height-top.getHeight()+6 );
        stage.addActor(top);
        Image topLabel=null;
        if(con.Max==2)
        	topLabel=new Image(atlsa.findRegion("twoman"));
        else
        	topLabel=new Image(atlsa.findRegion("threeman"));
        topLabel.setPosition(width/2-topLabel.getWidth()/2,height-top.getHeight()+20 );
        stage.addActor(topLabel);
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
        //字体
        bitmapFont = new BitmapFont(Gdx.files.internal("data/word.fnt"), Gdx.files.internal("data/word.png"), false);
        style = new LabelStyle(bitmapFont, bitmapFont.getColor()); 
       
        //按鈕
        NinePatch n1 = new NinePatch(atlsa.findRegion("btn2Up"), 14, 14, 14,
        	    14);
        NinePatch n2 = new NinePatch(atlsa.findRegion("btn2Down"), 14, 14, 14,
        	    14);
        Button.ButtonStyle btnStyle=new Button.ButtonStyle(new Button.ButtonStyle(new NinePatchDrawable(n1),
        		new NinePatchDrawable(n2),new NinePatchDrawable(n1))); 
        Button createRoom=new Button(btnStyle);
        //创建房间按钮
        Image createRoomLabel=new Image(atlsa.findRegion("createRoom"));
        createRoomLabel.setPosition(createRoom.getWidth()/2-createRoomLabel.getWidth()/2, 
        		createRoom.getHeight()/2-createRoomLabel.getHeight()/2);
        createRoom.setWidth(120);
        createRoom.setHeight(55);
        createRoom.add(createRoomLabel);
        createRoom.setPosition(width/2-createRoom.getWidth()/2, 10);
        stage.addActor(createRoom);
        createRoom.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				
				ma.hg.setScreen(ma.ns);
				con.createRoom();
				//创建房间
				super.clicked(event, x, y);
			}
    		
    	});
        //初始化桌子
        for(int i=0;i<8;i++)
        {
        	label[i]=new Label("房间"+toBig(i+1)+" ("+0+"/"+con.Max+")", style);
        }
        for(int i=0;i<8;i++)
        {
        	button[i]=new Button(btnStyle);
        	button[i].setVisible(false);
        	button[i].addActor(label[i]);
        	button[i].setName(Integer.toString(i));
        	button[i].addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					Button btn=(Button)event.getListenerActor();	
					//进入房间
					if(con.roomList.get( Integer.parseInt(btn.getName()) ).getUserCount()<con.Max)
					{
						ma.hg.setScreen(ma.ns);
						con.joinRoom(con.roomList.get( Integer.parseInt(btn.getName()) ));
					}else
						out("人数已满");
					super.clicked(event, x, y);
				}
        		
        	});
        	button[i].setWidth(170);
        	button[i].setHeight(60);
        	label[i].setPosition(button[i].getWidth()/2-label[i].getWidth()/2, 
        			button[i].getHeight()/2-label[i].getHeight()/2);
        	float x;
        	if(i<4)
        	{
        		x=250-button[i].getWidth()/2;
        	}else
        	{
        		x=550-button[i].getWidth()/2;
        	}
        	button[i].setPosition(x, 320-(i%4)*80);
        	stage.addActor(button[i]); 
        }
        //设置能够接受用户输入
        Gdx.input.setInputProcessor(stage);
        s=Server.getServer();
        //开启数据进程
        con.me = new User(ScoreManager.getName(ScoreManager.getScore()),Proxys.getLocalHostIp());
        con.requestRoom();
	}
	//返回大写
	public String toBig(int i)
	{
		String s="一";
		if(i==1)
			s="一";
		if(i==2)
			s="二";
		if(i==3)
			s="三";
		if(i==4)
			s="四";
		if(i==5)
			s="五";
		if(i==6)
			s="六";
		if(i==7)
			s="七";
		if(i==8)
			s="八";
		return s;
	}
	@Override
	public void dispose() {
		
	    stage.dispose();
	    s.dispose();
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
				case Proxys.MES_UPDATE_ROOM:
					showRoomList();
					break;
			}
		}
	};
	public void showRoomList()
	{
		for(int i=0;i<8;i++)
			button[i].setVisible(false);
		for(int i=0;i<con.roomList.size();i++)
		{
			button[i].setVisible(true);
			label[i].setText("房间"+toBig(i+1)+" ("+con.roomList.get(i).userCount+"/"+con.Max+")");
		}
	}
	public static void out(String s) {
		Log.v("test", s);
	}
}
