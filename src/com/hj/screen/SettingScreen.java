package com.hj.screen;

import javax.microedition.khronos.opengles.GL10;


import android.os.Build;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


import com.hj.net.WTOperateEnum;
import com.hj.net.WifiAdmin;
import com.hj.singlejoker.AD;
import com.hj.singlejoker.HjGame;
import com.hj.singlejoker.MainActivity;
import com.hj.singlejoker.ScoreManager;
import com.hj.singlejoker.SoundManager;




public class SettingScreen implements Screen{

	Stage stage;
	public MainActivity ma;
	TextureAtlas atlsa;
	BitmapFont bitmapFont;
	LabelStyle style;
	float width=HjGame.Width,height=HjGame.Height;
	Button.ButtonStyle btnStyle;
	Button btn[]=new Button[4];
	CheckBox cb[]=new CheckBox[2]; 
	//WiFi
	private WifiAdmin m_wiFiAdmin;
	private int wTOperateEnum = WTOperateEnum.NOTHING;
	public static final String WIFI_AP_HEADER = "l_";
	public static final String WIFI_AP_PASSWORD ="12345678";
	public static final int m_nCreateAPResult = 3;// 创建热点结果
	Label showTextLabel,scoreTextLabel,soundTextLabel;

	public SettingScreen(MainActivity ma)
	{
		this.ma=ma;
		m_wiFiAdmin = WifiAdmin.getInstance(ma);
	}
	@Override
	public void show() {
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
        //字体
        bitmapFont = new BitmapFont(Gdx.files.internal("data/word.fnt"), Gdx.files.internal("data/word.png"), false);
        style = new LabelStyle(bitmapFont, bitmapFont.getColor()); 
        //按鈕
        NinePatch n1 = new NinePatch(atlsa.findRegion("bgRoom_up"), 14, 14, 14,
        	    14);
        NinePatch n2 = new NinePatch(atlsa.findRegion("bgRoom_down"), 14, 14, 14,
        	    14);
        btnStyle=new Button.ButtonStyle(new Button.ButtonStyle(new NinePatchDrawable(n1),
        		new NinePatchDrawable(n2),new NinePatchDrawable(n1))); 
        //Copy
        Image imageCopy=new Image(atlsa.findRegion("copy"));
        imageCopy.setPosition(width/2-imageCopy.getWidth()/2, 50);
        stage.addActor(imageCopy);
        // GameUI
        //TopBar
        Image top=new Image(atlsa.findRegion("top"));
        top.setPosition(width/2-top.getWidth()/2,height-top.getHeight()+6 );
        stage.addActor(top);
        Image topLabel=new Image(atlsa.findRegion("detail"));
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
        createList();
        
        //设置能够接受用户输入
        Gdx.input.setInputProcessor(stage);
	}
	public void createList()
	{
		Group gp=new Group();
		Image page=new Image(atlsa.findRegion("bgSettingpage"));
		page.setBounds(0,0,230,230);
		gp.setPosition(width/7, height/2-115);
		gp.addActor(page);
		stage.addActor(gp);
		//加入按鈕

		Image im[]={
				new Image(atlsa.findRegion("advice")),
				new Image(atlsa.findRegion("sysVoice")),
				new Image(atlsa.findRegion("myscore")),
				new Image(atlsa.findRegion("createwifi"))
		};
		//wifiTips
        showTextLabel=new Label("No WiFi",style);
        stage.addActor(showTextLabel);
        showTextLabel.setPosition( width/2-30, 310);
        //ScoreTips
        
        scoreTextLabel=new Label(ScoreManager.getScore()+"<"+ScoreManager.getName(ScoreManager.getScore())+">"
        		+" (黄金以上去广告)",style);
        //scoreTextLabel=new Label(ScoreManager.getScore()+"<"+ScoreManager.getName(ScoreManager.getScore())+">"
        //		,style);
        scoreTextLabel.setVisible(false);
        stage.addActor(scoreTextLabel);
        scoreTextLabel.setPosition( width/2-30, 250);
        //背景音乐
        soundTextLabel=new Label("Music          Sound",style);
        soundTextLabel.setVisible(false);
        soundTextLabel.setPosition( width/2-30, 200);
        stage.addActor(soundTextLabel);
        CheckBox.CheckBoxStyle cstyle=new CheckBox.CheckBoxStyle(
        		new TextureRegionDrawable(atlsa.findRegion("on")), 
        		new TextureRegionDrawable(atlsa.findRegion("off")), 
        		new BitmapFont(), new Color());
        cb[0]=new CheckBox("music", cstyle);
        cb[1]=new CheckBox("sound", cstyle);
        if(!SoundManager.openMusic)
        	cb[0].setChecked(true);
        if(!SoundManager.openSound)
        	cb[1].setChecked(true);
        for(int i=0;i<2;i++)
        {
        	cb[i].setBounds(width/2+200*i+50,190,115,50);
        	cb[i].setVisible(false);
        	cb[i].addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					CheckBox c=(CheckBox)event.getListenerActor();
					if(c==cb[0])
					{
						SoundManager.openMusic=!SoundManager.openSound;
						if(c.isChecked())
							SoundManager.CloseBackMusic();
						else
							SoundManager.StartBackMusic();
					}
					if(c==cb[1])
					{
						SoundManager.openSound=!SoundManager.openSound;
					}
					super.clicked(event, x, y);
				}
        		 
               
            }); 
        	stage.addActor(cb[i]);
        }
		for(int i=0;i<4;i++)
		{
			btn[i]=new ImageButton(new TextureRegionDrawable(atlsa.findRegion("bgsettingpageUp")),
					new TextureRegionDrawable(atlsa.findRegion("bgSettingpageDown")));
	        btn[i].setBounds(10, 55*i,200,70);
	        im[i].setBounds(btn[i].getWidth()/2-65, btn[i].getHeight()/2-16, 130, 33);
	        gp.addActor(btn[i]);
	        btn[i].addActor(im[i]);
	        btn[i].addListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					Button button=(Button)event.getListenerActor();
					if(button==btn[0])//推荐
					{
						AD.showWandoujiaList();
					}
					if(button==btn[1])//音效
					{
						soundTextLabel.setVisible(true);
						cb[0].setVisible(true);
						cb[1].setVisible(true);
					}
					if(button==btn[2])//分数
					{
						scoreTextLabel.setVisible(true);
					}
					if(button==btn[3])//Wifi
					{
						createWifi();
					}
					super.clicked(event, x, y);
				}
	        	
	        });
		}
	}
	public void createWifi()
	{
		
		
		//创建wifi
		if (m_wiFiAdmin.getWifiApState() == 4) {
			
			showTextLabel.setText("Error");
			return;
		}
		if (m_wiFiAdmin.mWifiManager.isWifiEnabled()) {
			wTOperateEnum = WTOperateEnum.CREATE;
		
			//return;
		}
		if (((m_wiFiAdmin.getWifiApState() == 3) || (m_wiFiAdmin
				.getWifiApState() == 13))
				&& (!m_wiFiAdmin.getApSSID().startsWith(WIFI_AP_HEADER))) {
			wTOperateEnum = WTOperateEnum.CREATE;
			
			showTextLabel.setText("System already has one");
			return;
		}
		if (((m_wiFiAdmin.getWifiApState() == 3) || (m_wiFiAdmin
				.getWifiApState() == 13))
				&& (m_wiFiAdmin.getApSSID().startsWith(WIFI_AP_HEADER))) {
			wTOperateEnum = WTOperateEnum.CLOSE;
			showTextLabel.setText("Name: "+m_wiFiAdmin.getApSSID()+", Password: "+WIFI_AP_PASSWORD);
			return;
		}
		m_wiFiAdmin.closeWifi();
		m_wiFiAdmin.createWiFiAP(
				m_wiFiAdmin.createWifiInfo(WIFI_AP_HEADER
						+ Build.MODEL.substring(0, 5), WIFI_AP_PASSWORD, 3, "ap"),
				true);
		while(true)
		{
			try{
			if(m_wiFiAdmin.getApSSID().startsWith(WIFI_AP_HEADER))
				{
					showTextLabel.setText("Name:"+m_wiFiAdmin.getApSSID()+", Password:"+WIFI_AP_PASSWORD);
					break;
				}
			Thread.sleep(1000);
			}catch(Exception e){};
		}
        
	}
	@Override
	public void dispose() {
	
	    stage.dispose();
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

}
