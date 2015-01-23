package com.hj.net;


public class User implements java.io.Serializable {

	private String name;
	private String ip="null";
	private int roomId=-1,deskId=-1,isReady=0;
	private int landlordScore=-1;
	public User(String name,String ip,int deskId,int RoomId,int isReady,int lanlordscore)
	{
		this.name=name;
		this.ip=ip;
		this.deskId=deskId;
		this.roomId=RoomId;
		this.isReady=isReady;
		this.landlordScore=lanlordscore;
	}
	public User(){};
	public int getIsReady() {
		return isReady;
	}
	public void setIsReady(int isReady) {
		this.isReady = isReady;
	}
	
	
	public int getLandlordScore() {
		return landlordScore;
	}
	public void setLandlordScore(int landlordScore) {
		this.landlordScore = landlordScore;
	}
	public int getDeskId() {
		return deskId;
	}

	public void setDeskId(int deskId) {
		this.deskId = deskId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public User(String name, String ip) {
		super();
		this.name = name;

		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
