package com.hj.net;

public class Room implements java.io.Serializable{

	public String ip;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getUserCount() {
		return userCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public int userCount;
	
	public Room(String ip,int userCount)
	{
		this.ip=ip;
		this.userCount=userCount;
	}
}
