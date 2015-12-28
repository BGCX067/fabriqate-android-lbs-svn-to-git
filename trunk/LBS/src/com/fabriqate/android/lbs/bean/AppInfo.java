package com.fabriqate.android.lbs.bean;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Log;

public class AppInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1859792224198810327L;
	public static final String TYPE_POSITION="position";
	public static final String TYPE_WEATHER="weather";
	public static final String TYPE_TIME="time";
	private int  app_id;
	private String name;
	private String description;
	private String app_url;
	private String logo;
	private String price;
	private String rate;
	private String caption;
	private String type;//position ,time,weather
	private ArrayList<String> image;
	
//	private Bitmap logoBitmap;
	public int getApp_id() {
		return app_id;
	}
	public void setApp_id(int app_id) {
		this.app_id = app_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getApp_url() {
		return app_url;
	}
	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<String> getImage() {
		return image;
	}
	
	public void setImage(ArrayList<String> image) {
		this.image = image;
	}
	
/*	public void setLogoBitmap (Bitmap logoBitmap) {
		this.logoBitmap = logoBitmap;
	}

	public Bitmap getLogoBitmap () {
		return logoBitmap;
	}*/
	public AppInfo() {
		super();
	
	}
	public AppInfo(int app_id,String name, String description, String app_url,
			String logo, String price, String rate, String caption,String type,
			ArrayList<String> image) {
		super();
		this.app_id=app_id;
		this.name = name;
		this.description = description;
		this.app_url = app_url;
		this.logo = logo;
		this.price = price;
		this.rate = rate;
		this.caption = caption;
		this.type=type;
		this.image = image;
	//	this.logoBitmap=logoBitmap;
	}
	@Override
	public String toString() {
		return "AppInfo [name=" + name + ", description=" + description
				+ ", app_url=" + app_url + ", logo=" + logo + ", price="
				+ price + ", rate=" + rate + ", caption=" + caption +", type="+type
				+ ", image=" + image  + "]";
	}
	@Override
	public boolean equals(Object o) {
		if(!( o instanceof AppInfo) )	
		{
			return false;
		}
		AppInfo other= (AppInfo) o;
		if(this.app_id==other.app_id  )
		{
			return true;
		}
		
		
		/*if(this.name .equals(other.name) && this.app_url .equals(other.app_url) && this.type.equals(other.type) && this.price.equals(other.price) )
		{
			return true;
		}*/
		return false;
	}
	
	
	
	
	
	
}
