package com.hj.tool;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;


public class Card extends Actor {
 
	//图片
	public TextureRegion mTextureRegion;   
	TextureAtlas atlsa;
	//名字
	public int imageId;
	public int color;//黑红樱方 1,2,3,4
	public int value;//面值
	//移动
	public int clicked=0;
	public int touched=0;
	//背面
	public boolean isFront=true;
	
	public Card(int imageId,TextureAtlas atlsa)
	{
		this.imageId=imageId;
		this.atlsa=atlsa;
		mTextureRegion=atlsa.findRegion("c"+imageId); 
		this.color=(imageId-1)/13+1;
		this.value=(imageId-1)%13+1;
		if(this.value<=2)this.value+=13;
		if(imageId==53||imageId==54)
		{
			this.color=5;
			this.value=16-53+imageId;
		}
		//重载大小，防止监听无效
		setWidth(mTextureRegion.getRegionWidth());
		setHeight(mTextureRegion.getRegionHeight());
	}
	//移动
	public void move()
	{
		if(clicked==0)
			setY(getY()+getHeight()/3);
		if(clicked==1&&(getY()>25))
			setY(getY()-getHeight()/3);
		clicked=(clicked+1)%2;
	}
	public void changeOrient()
	{
		if(isFront)
			setFront();
		else
			setBack();
	}
	//设置背面
	public void setBack()
	{
		mTextureRegion=atlsa.findRegion("c55");
	}
	//设置正面
	public void setFront()
	{
		mTextureRegion=atlsa.findRegion("c"+imageId);
	}
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {

		batch.draw(mTextureRegion, getX(), getY(),  
                mTextureRegion.getRegionWidth() / 2,  
                mTextureRegion.getRegionHeight() / 2,  
                mTextureRegion.getRegionWidth(),  
                mTextureRegion.getRegionHeight(), getScaleX(), getScaleY(),  
                getRotation());  
		super.draw(batch, parentAlpha);
	}

}
