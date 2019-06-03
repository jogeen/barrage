package com.jogeen.barrage.server.model;

import java.awt.Color;

import com.jogeen.barrage.common.GiftEnum;

/**
 * 
 * 弹幕数据类
 * @version 1.0
 * @author jogeen
 *
 */
public class GiftBarrage extends BaseBarrage { 
	
	private String imageName;
	private String num;
	
	public GiftBarrage(String sender,String imageName) {
		this.color=Color.GREEN;
		this.sendser=sender;
		this.imageName = imageName;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
	
	public String getThanksMessage() {
		return String.format(GiftEnum.getThanksMessageByType(imageName),getSendser());
	}
	
	

}
