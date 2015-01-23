package com.hj.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.hj.screen.NetScreen;
import com.hj.singlejoker.HjGame;
import com.hj.tool.Card;
import com.hj.tool.Comm;
import com.hj.tool.GameInfo;

public class RightPlayer implements INetPlayer{

	GameInfo gInfo;
	NetScreen screen;
	TextureAtlas atlsa=HjGame.getManager().get("data/resource.txt",TextureAtlas.class);
	public RightPlayer(GameInfo gInfo,NetScreen screen)
	{
		this.gInfo=gInfo;
		this.screen=screen;
	}
	@Override
	public void showName(String name) 
	{
		gInfo.name[2].setVisible(true);
		gInfo.name[2].setText(name);
	}
	public void showReady(int b)
	{
		if(b==1)
			gInfo.readyLabelFinished[2].setVisible(true);
		else
			gInfo.readyLabelFinished[2].setVisible(false);
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
			cards.addAction(Actions.moveTo(740,310,1f));
			cards.setScale(0.5f);
			cards.setBack();
		}
		Comm.setOrder(gInfo.playerList[playerID]);
		Comm.rePosition(gInfo.playerList[playerID], 2);
		
	}
	@Override
	public void showLandlordScore(int score) {
		if(score>-1)
		{
			//œ‘ æ∑÷ ˝
			switch(score)
			{
				case 0:gInfo.showScoreLabel[2]=new Image(atlsa.findRegion("no1"));
					break;
				case 1:gInfo.showScoreLabel[2]=new Image(atlsa.findRegion("one"));
					break;
				case 2:gInfo.showScoreLabel[2]=new Image(atlsa.findRegion("two"));
					break;
				case 3:gInfo.showScoreLabel[2]=new Image(atlsa.findRegion("three"));
					break;
			}
			gInfo.showScoreLabel[2].setVisible(true);
			gInfo.showScoreLabel[2].setPosition(640,350);
			screen.stage.addActor(gInfo.showScoreLabel[2]);
		
		}else
		{
			if(gInfo.showScoreLabel[2]!=null)
				gInfo.showScoreLabel[2].setVisible(false);
		}
	}

}
