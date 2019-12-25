package com.jogeen.barrage.server.data;

import com.jogeen.barrage.server.config.ServerConfig;
import com.jogeen.barrage.server.model.GiftBarrage;
import com.jogeen.barrage.server.model.TextBarrage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author jogeen
 * @version 1.0
 * @date 2018年10月14日
 */
public class ServerDataCenter {
	

	/**
	 * 文字弹幕列表
	 */
	public static List<TextBarrage> TEXT_BARRAGE_LIST = new ArrayList<TextBarrage>();
	

	/**
	 * 礼物弹幕列表
	 */
	public static List<GiftBarrage> GIFT_BARRAGE_LIST = new ArrayList<GiftBarrage>();

	/**
	 * 向弹幕列表添加弹幕
	 * 
	 * @param c
	 */
	public static void add(TextBarrage c) {
		Random random = new Random();
		c.y = random.nextInt(ServerConfig.WINDOW_HEIGHT - 150) + 50;
		c.x = ServerConfig.WINDOW_WIDTH;
		TEXT_BARRAGE_LIST.add(c);

	}

	/**
	 * 向弹幕列表添加弹幕
	 * 
	 * @param c
	 */
	public static void add(GiftBarrage c) {
		Random random = new Random();
		c.y = random.nextInt(ServerConfig.WINDOW_HEIGHT - 150) + 50;
		c.x = ServerConfig.WINDOW_WIDTH;
		GIFT_BARRAGE_LIST.add(c);
	}

}
