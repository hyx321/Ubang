package com.ubang.huang.ubangapp.bean;

/**
 * 目的是为了查询个人的求助
 */
public class PersonSeekHelp {
	private int userID;
	private String status;
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "{\"userID\":\"" + userID + "\", \"status\":\"" + status + "\"}";
	}
	
}
