package com.hj.singlejoker;


import com.hj.tool.Comm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ScoreManager {

	public static MainActivity ma; 
	public static SharedPreferences preferences;
	public static Editor editor;
	public static String data="score";
	public static int baseScore=0,baseTimes=1,base=10;
	public static void init(MainActivity ma)
	{
		ScoreManager.ma=ma;
		preferences=ma.getSharedPreferences("Joker_user",Context.MODE_PRIVATE);
		editor=preferences.edit();
	}
	public static void setScore(int score)
	{
		editor.putInt(data, score);
		editor.commit();
		Comm.HjLog("得分:"+score);
	}
	public static int getScore()
	{
		if(preferences.getInt(data, 4000)<4000)
			return 4000;
		else
			return preferences.getInt(data, 4000);
	}
	public static String getName(int s)
	{
		if(s<8000)
			return "黄铜"+(5-(s%4000)/800);
		else if(s<16000)
			return "白银"+(5-(s%8000)/1600);
		else if(s<32000)
			return "黄金"+(5-(s%16000)/3200);
		else if(s<64000)
			return "白金"+(5-(s%32000)/6400);
		else if(s<128000)
			return "钻石"+(5-(s%64000)/12800);
		else
			return "王者";
	}
}
