package com.hj.singlejoker;

import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.hj.tool.Card;
import com.hj.tool.CardType;
import com.hj.tool.Comm;

public class SoundManager {
	//要记得释放资源
	public static AssetManager m=HjGame.getManager();
	public static Music backgroundMusic;
	public static Sound sc1[]=new Sound[18];
	public static Sound sc2[]=new Sound[16];
	public static Sound sc3[]=new Sound[16];
	public static Sound sc4[]=new Sound[2];
	public static Sound sc0;
	public static boolean openSound=true,openMusic=true;
	public static void Init()
	{
		backgroundMusic = m.get("audio/bgVoice.ogg", Music.class);
		backgroundMusic.setLooping(true); //循环播放
		backgroundMusic.setVolume(0.5f); //设置音量
		//c1
		for(int i=3;i<18;i++)
			sc1[i]=m.get("audio/single"+i+".ogg",Sound.class);
		//c2 c3
		for(int i=3;i<16;i++)
		{
			sc2[i]=m.get("audio/pair"+i+".ogg",Sound.class);
			sc3[i]=m.get("audio/three"+i+".ogg",Sound.class);
		}
		//c4
		sc4[0]=m.get("audio/bomb.ogg",Sound.class);
		sc4[1]=m.get("audio/bombjoker.ogg",Sound.class);
		//c0
		sc0=m.get("audio/pass.ogg",Sound.class);
	}
	public static void StartBackMusic()
	{
		if(openMusic)
			backgroundMusic.play(); //播放
	}
	public static void CloseBackMusic()
	{
		backgroundMusic.pause();
	}
	public static void PlaySound(List<Card> list)
	{
		if(openSound)
		{
			CardType ct=Comm.jugdeType(list);
			switch(ct)
			{
				case c1:
					sc1[Comm.getValue(list.get(0))].play();break;
				case c2:
					sc2[Comm.getValue(list.get(0))].play();break;
				case c3:
					sc3[Comm.getValue(list.get(0))].play();break;
				case c4:
					if(list.size()!=2)
						sc4[0].play();
					else
						sc4[1].play();
					break;
				case c0:
					sc0.play();break;
				case c123:
					m.get("audio/straight.ogg",Sound.class).play();break;
				case c1122:
					m.get("audio/pairstraight.ogg",Sound.class).play();break;
				case c1112223344:
				case c11122234:
				case c111222:
					m.get("audio/threestraight.ogg",Sound.class).play();break;
				case c32:
					m.get("audio/threewithpair.ogg",Sound.class).play();break;
				case c31:
					m.get("audio/threewithsingle.ogg",Sound.class).play();break;
				case c411:
					m.get("audio/fourwithsingle.ogg",Sound.class).play();break;
				case c422:
					m.get("audio/fourwithpair.ogg",Sound.class).play();break;
				
			}
		}
		
	}
	public static void PlayWin(boolean flag)
	{
		if(openSound)
		{
			if(flag)
				m.get("audio/win.ogg",Sound.class).play();
			else
				m.get("audio/lose.ogg",Sound.class).play();
		}
		
	}
	public static void PlayStart()
	{
		if(openSound)
			m.get("audio/start.ogg",Sound.class).play();
	}
	public static void PlayRemain(int i)
	{
		if(openSound)
		{
			if(i==2||i==1)
			m.get("audio/remain"+i+".ogg",Sound.class).play();
		}
	}
	public static void PlayBid(int i)
	{
		if(openSound)
			m.get("audio/bid"+i+".ogg",Sound.class).play();
	}
	public static void PlayPass()
	{
		if(openSound)
			sc0.play();
	}
}
