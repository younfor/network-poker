package com.hj.singlejoker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class HjGame extends Game{

	Screen screen;
	//资源
	public static AssetManager manager;
	public static AssetManager getManager() {
	    if (manager == null) {
	        manager = new AssetManager();
	    }
	    return manager;
	}
	//参数
	public static int Height=480,Width=800;
	@Override
	public void create() 
	{
		setScreen(screen);
		
	}
	public HjGame(Screen screen)
	{
		this.screen=screen;
	}
	public static void load(AssetManager m)
	{
		m.load("data/bg.txt", TextureAtlas.class);
		m.load("data/resource.txt", TextureAtlas.class);
		m.load("audio/bgVoice.ogg",Music.class);
		//audio
		cLoad(m, "bid", 0, 3);
		cLoad(m, "bomb", 0, 0);
		cLoad(m, "bombjoker", 0, 0);
		cLoad(m, "follow", 1, 3);
		cLoad(m, "fourwithpair", 0, 0);
		cLoad(m, "fourwithsingle", 0, 0);
		cLoad(m, "lose", 0, 0);
		cLoad(m, "pair", 3, 15);
		cLoad(m, "pairstraight", 0, 0);
		cLoad(m, "pass", 0,0);
		cLoad(m, "remain", 1, 2);
		cLoad(m, "single", 3, 17);
		cLoad(m, "start", 0,0);
		cLoad(m, "straight", 0,0);
		cLoad(m, "three", 3,15);
		cLoad(m, "threestraight", 0,0);
		cLoad(m, "threewithpair", 0,0);
		cLoad(m, "threewithsingle", 0,0);
		cLoad(m, "win", 0,0);
		
	}
	public static void cLoad(AssetManager m,String name,int s,int e)
	{
		if(e==0)
			m.load("audio/"+name+".ogg",Sound.class);
		else
		{
			for(int i=s;i<=e;i++)
				m.load("audio/"+name+i+".ogg",Sound.class);
		}
		
	}
	public static void unload(AssetManager m)
	{
		m.unload("data/bg.txt");
		m.unload("data/resource.txt");
		m.unload("audio/bgVoice.ogg");
		//audio
		cunLoad(m, "bid", 0, 3);
		cunLoad(m, "bomb", 0, 0);
		cunLoad(m, "bombjoker", 0, 0);
		cunLoad(m, "follow", 1, 3);
		cunLoad(m, "fourwithpair", 0, 0);
		cunLoad(m, "fourwithsingle", 0, 0);
		cunLoad(m, "lose", 0, 0);
		cunLoad(m, "pair", 3, 15);
		cunLoad(m, "pairstraight", 0, 0);
		cunLoad(m, "pass", 0,0);
		cunLoad(m, "remain", 1, 2);
		cunLoad(m, "single", 3, 17);
		cunLoad(m, "start", 0,0);
		cunLoad(m, "straight", 0,0);
		cunLoad(m, "three", 3,15);
		cunLoad(m, "threestraight", 0,0);
		cunLoad(m, "threewithpair", 0,0);
		cunLoad(m, "threewithsingle", 0,0);
		cunLoad(m, "win", 0,0);
		//while(!m.update());
	}
	public static void cunLoad(AssetManager m,String name,int s,int e)
	{
		if(e==0)
			m.unload("audio/"+name+".ogg");
		else
		{
			for(int i=s;i<=e;i++)
				m.unload("audio/"+name+i+".ogg");
		}
		
	}
}
