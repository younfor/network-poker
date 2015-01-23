package com.hj.player;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.hj.screen.NetScreen;
import com.hj.singlejoker.HjGame;
import com.hj.tool.Card;
import com.hj.tool.Comm;
import com.hj.tool.GameInfo;

public class MidPlayer implements INetPlayer{

	GameInfo gInfo;
	NetScreen screen;
	TextureAtlas atlsa=HjGame.getManager().get("data/resource.txt",TextureAtlas.class);
	public MidPlayer(GameInfo gInfo,NetScreen screen)
	{
		this.gInfo=gInfo;
		this.screen=screen;
	}
	@Override
	public void showName(String name) {
		
			gInfo.name[1].setVisible(true);
			gInfo.name[1].setText(name);
		
	}
	public void showReady(int b)
	{
		if(b==1)
			gInfo.readyLabelFinished[1].setVisible(true);
		else
			gInfo.readyLabelFinished[1].setVisible(false);
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
			cards.addAction(Actions.moveTo(50 + i * 39, 20, 1f));
			
		}
		
		Comm.setOrder(gInfo.playerList[playerID]);
		Comm.rePosition(gInfo.playerList[playerID], 1);
	}
	@Override
	public void showLandlordScore(int score) {

		if(score>-1)
		{
			//œ‘ æ∑÷ ˝
			switch(score)
			{
				case 0:gInfo.showScoreLabel[1]=new Image(atlsa.findRegion("no1"));
					break;
				case 1:gInfo.showScoreLabel[1]=new Image(atlsa.findRegion("one"));
					break;
				case 2:gInfo.showScoreLabel[1]=new Image(atlsa.findRegion("two"));
					break;
				case 3:gInfo.showScoreLabel[1]=new Image(atlsa.findRegion("three"));
					break;
			}
			gInfo.showScoreLabel[1].setVisible(true);
			gInfo.showScoreLabel[1].setPosition(screen.width/2-gInfo.showScoreLabel[1].getWidth()/2,170 );
			screen.stage.addActor(gInfo.showScoreLabel[1]);
		
		}else
		{
			
			if(gInfo.showScoreLabel[1]!=null)
				gInfo.showScoreLabel[1].setVisible(false);
		}
	}

}
