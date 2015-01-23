package com.hj.net;

public class CardData implements java.io.Serializable {

	public CardData(int ImageId,int deskId) {
		this.imageId = ImageId;
		this.deskId=deskId;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	// Ãû×Ö
	public int imageId;
	// ×ÀºÅ
	public int deskId;

	public int getDeskId() {
		return deskId;
	}

	public void setDeskId(int deskId) {
		this.deskId = deskId;
	}
}
