package com.hj.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.hj.net.Constant;
import com.hj.screen.NetScreen;
import com.hj.singlejoker.HjGame;
import com.hj.tool.Card;
import com.hj.tool.Comm;
import com.hj.tool.GameInfo;

public class LeftPlayer implements INetPlayer{

	GameInfo gInfo;
	NetScreen screen;
	TextureAtlas atlsa=HjGame.getManager().get("data/resource.txt",TextureAtlas.class);
	public LeftPlayer(GameInfo gInfo,NetScreen screen)
	{
		this.gInfo=gInfo;
		this.screen=screen;
	}
	@Override
	public void showName(String name)
	{
			gInfo.name[0].setVisible(true);
			gInfo.name[0].setText(name);
		
	}
	public void showReady(int b)
	{
		if(b==1)
			gInfo.readyLabelFinished[0].setVisible(true);
		else
			gInfo.readyLabelFinished[0].setVisible(false);
	}
	@Override
	public void showOpenCard(int playerID) 
	{
		for(int i=0;i<gInfo.playerList[playerID].size();i++)
		{
			Card cards=gInfo.playerList[playerID].get(i);
			cards.setZIndex(i);
			cards.setPosition(screen.width / 2 - cards.mTextureRegion.getRegionWidth() / 2, screen.height
					/ 2 - cards.mTextureRegion.getRegionHeight() / 2);
			screen.stage.addActor(cards);
			cards.setScale(0.5f);
			cards.setBack();
			cards.addAction(Actions.moveTo(2,310, 1f));
		}
		
		Comm.setOrder(gInfo.playerList[playerID]);
		Comm.rePosition(gInfo.playerList[playerID], 0);
	}
	@Override
	public void showLandlordScore(int score) {
		
		if(score>-1)
		{
			//œ‘ æ∑÷ ˝
			switch(score)
			{
				case 0:gInfo.showScoreLabel[0]=new Image(atlsa.findRegion("no1"));
					break;
				case 1:gInfo.showScoreLabel[0]=new Image(atlsa.findRegion("one"));
					break;
				case 2:gInfo.showScoreLabel[0]=new Image(atlsa.findRegion("two"));
					break;
				case 3:gInfo.showScoreLabel[0]=new Image(atlsa.findRegion("three"));
					break;
			}
			gInfo.showScoreLabel[0].setVisible(true);
			gInfo.showScoreLabel[0].setPosition(100, 350);
			screen.stage.addActor(gInfo.showScoreLabel[0]);
		
		}else
		{
			if(gInfo.showScoreLabel[0]!=null)
				gInfo.showScoreLabel[0].setVisible(false);
		}
		
	}
	
	

}
