package com.hj.screen;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.hj.singlejoker.AD;
import com.hj.singlejoker.HjGame;
import com.hj.singlejoker.MainActivity;
import com.hj.singlejoker.SoundManager;

public class UiScreen implements Screen{

	Stage stage;
	private BitmapFont bitmapFont;
	LabelStyle style;
	Button button[]=new Button[4];
	public MainActivity ma;
	TextureAtlas atlsa;
	public UiScreen(MainActivity ma)
	{
		this.ma=ma;
		AD.ma=ma;
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
        //背景音乐
        if(SoundManager.backgroundMusic==null)
        {
	        SoundManager.Init();
	        SoundManager.StartBackMusic();
	    }
        //Copy
        Image imageCopy=new Image(atlsa.findRegion("copy"));
        imageCopy.setPosition(width/2-imageCopy.getWidth()/2, 50);
        stage.addActor(imageCopy);
        //logo
        Image imageLogo=new Image(atlsa.findRegion("bgUiLogo"));
        imageLogo.setPosition(width/4-imageLogo.getWidth()/2, height/2-imageLogo.getHeight()/2);
        stage.addActor(imageLogo);
        //按鈕
        NinePatch n1 = new NinePatch(atlsa.findRegion("btnUi_up"), 14, 14, 15,
        	    15);
        NinePatch n2 = new NinePatch(atlsa.findRegion("bgUi_down"), 14, 14, 15,
        	    15);
        Button.ButtonStyle btnStyle=new Button.ButtonStyle(new Button.ButtonStyle(new NinePatchDrawable(n1),
        		new NinePatchDrawable(n2),new NinePatchDrawable(n1))); 
        Image btnImage[]={
        		new Image(atlsa.findRegion("oneman")),
        		new Image(atlsa.findRegion("twoman")),
        		new Image(atlsa.findRegion("threeman")),
        		new Image(atlsa.findRegion("detailsetting"))
        };
        for(int i=0;i<4;i++)
        {
        	button[i]=new Button(btnStyle);
        	float offsety=(height-7*button[i].getHeight())/6;
        	button[i].addActor(btnImage[i]);
        	button[i].setWidth(width/3);
        	btnImage[i].setPosition(button[i].getWidth()/2-btnImage[i].getWidth()/2, 
        			button[i].getHeight()/2-btnImage[i].getHeight()/2);
        	button[i].setPosition(width*3/4-button[i].getWidth()/2, height-(i+2)*(offsety+button[i].getHeight()));
        	stage.addActor(button[i]); 
        }
        button[0].addListener(new ClickListener() {

    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			//单人对战
    			ma.con.Max=1;
    			ma.hg.setScreen(ma.ms);
    		}});
        button[1].addListener(new ClickListener() {

    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			
    			//双人联机
    			ma.con.Max=2;
    			ma.con.roomList.clear();
    			ma.hg.setScreen(ma.rs);
    		}});
        button[2].addListener(new ClickListener() {

    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			
    			//三人联机
    			ma.con.Max=3;
    			ma.con.roomList.clear();
    			ma.hg.setScreen(ma.rs);
    		}});
        button[3].addListener(new ClickListener() {

    		@Override
    		public void clicked(InputEvent event, float x, float y) {
    			
    			//详细设置

    			ma.hg.setScreen(ma.ss);
    			
    				Log.v("test", "btn3");
    		}});
        //设置能够接受用户输入
        Gdx.input.setInputProcessor(stage);
	}
	@Override
	public void dispose() {
		
		bitmapFont.dispose();
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
