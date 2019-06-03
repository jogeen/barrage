package com.jogeen.barrage.server.model;

import java.awt.Color;

import com.jogeen.barrage.server.config.ServerConfig;

public class BaseBarrage {
	public static final int DEFUALT_SIZE = 40;
	public static final int DEFUALT_STYLE = 1;
	public static final int DEFUALT_SPEED = 1;

	public int x = 0;
	public int y = 0;
	public int speed = ServerConfig.SPEED;

	protected Color color = Color.BLUE;;
	protected String text;
	protected int size;
	protected int style;
	protected String sendser;
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public String getSendser() {
		return sendser;
	}

	public void setSendser(String sendser) {
		this.sendser = sendser;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}
	
	

}
