package com.jogeen.barrage.server.model;

import java.awt.Color;

/**
 * 
 * 弹幕数据类
 * 
 * @version 1.0
 * @author jogeen
 *
 */
public class TextBarrage extends BaseBarrage {

	public TextBarrage(String sender,String text) {
		if (text.contains("?") || text.contains("？")) {
			this.color = Color.RED;
		}
		this.size = DEFUALT_SIZE;
		this.style = DEFUALT_STYLE;
		this.speed = DEFUALT_SPEED;
		this.text = text;
		this.sendser=sender;
	}

	public TextBarrage(Color color, int size, int style, String text) {
		this.color = color;
		this.size = size;
		this.style = style;
		this.text = text;
	}
}
