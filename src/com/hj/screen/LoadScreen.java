package com.hj.screen;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
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

public class LoadScreen implements Screen{

	Stage stage;
	BitmapFont bitmapFont;
	LabelStyle style;
	Label loadLabel;
	MainActivity ma;
	public LoadScreen(MainActivity ma)
	{
		this.ma=ma;
		AD.ma=ma;
	}
	@Override
	public void show() {
		//开始
		float width = 800;
        float height = 480; 
    	//舞台
        stage = new Stage(width, height,true);  
        //资源
        AssetManager manager = HjGame.getManager();
        HjGame.load(manager);
        //背景
        bitmapFont = new BitmapFont();
        style = new LabelStyle(bitmapFont, bitmapFont.getColor()); 
        loadLabel=new Label("loading... 0%", style);
        loadLabel.setPosition(width/2-loadLabel.getWidth()/2, height/3);
        Image imageBg = new Image(new Texture(Gdx.files.internal("data/loading.png")));  
        imageBg.setPosition(width/2-imageBg.getWidth()/2, height/2-imageBg.getHeight()/2);
        stage.addActor(imageBg); 
        stage.addActor(loadLabel);
        
       
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
        if (HjGame.getManager().update()) {
            ma.hg.setScreen(ma.us);
        }
        loadLabel.setText("loading... "+(int)(HjGame.getManager().getProgress()*100)+"%");
	}

	@Override
	public void resize(int arg0, int arg1) {
	
		
	}

	@Override
	public void resume() {
		
		
	}
	
}
