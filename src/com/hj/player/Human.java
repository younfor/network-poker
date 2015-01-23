package com.hj.player;

import com.hj.tool.Card;
import com.hj.tool.GameInfo;

public class Human implements IPlayer{

	GameInfo gInfo;
	public Human(GameInfo ginfo)
	{
		this.gInfo=ginfo;
		
	}
	//出牌
	@Override
	public void sendCard() {
		//隐藏之前别人的喊话
		//gInfo.showCancelLabel[0].setVisible(false);
		//gInfo.showCancelLabel[2].setVisible(false);
		//隐藏之前出的牌
		if(gInfo.playerOutList[1]!=null)
		{
			for(Card c:gInfo.playerOutList[1])
			{
				c.setVisible(false);
			}
			gInfo.playerOutList[1].clear();
		}
		gInfo.showCancelLabel[1].setVisible(false);
		//显示按钮
		for(int i=4;i<8;i++)
		{
			gInfo.button[i].setVisible(true);
		}
	}

}
