package com.ubang.huang.ubangapp.bean;

public class PictureURL {
	private int help_id;
	private String picture;
	public int getHelp_id() {
		return help_id;
	}
	public void setHelp_id(int help_id) {
		this.help_id = help_id;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	@Override
	public String toString() {
		return "{\"help_id\":\"" + help_id + "\", \"picture\":\"" + picture + "\"}";
	}
	
}
