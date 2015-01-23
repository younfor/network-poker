package com.hj.player;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.hj.singlejoker.SoundManager;
import com.hj.tool.Card;
import com.hj.tool.Comm;
import com.hj.tool.GameInfo;

public class Computer implements IPlayer{

	GameInfo gInfo;
	public Computer(GameInfo ginfo)
	{
		this.gInfo=ginfo;
		
	}
	@Override
	public void sendCard() {

		
		int id=gInfo.getCurrentPlayerId();
		if(gInfo.playerOutList[id]!=null)
		{
			for(Card c:gInfo.playerOutList[id])
			{
				c.setVisible(false);
			}
		}
		gInfo.playerOutList[id]=Comm.getBestAI(gInfo.playerList[id], gInfo.getOppo());
		if(gInfo.playerOutList[id]!=null)
		{
			gInfo.showCancelLabel[id].setVisible(false);
			gInfo.playerList[id].removeAll(gInfo.playerOutList[id]);
			if(gInfo.playerList[id].size()<3)
				SoundManager.PlayRemain(gInfo.playerList[id].size());
			Comm.setOutPosition(gInfo.playerOutList[id],id);
		}else
		{
			SoundManager.PlayPass();
			gInfo.showCancelLabel[id].setVisible(true);
		}
		gInfo.setCurrentPlayerId((id+1)%3);
		
	}

}
