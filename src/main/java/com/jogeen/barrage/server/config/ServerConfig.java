package com.jogeen.barrage.server.config;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jogeen.barrage.client.ClientMain;
import com.jogeen.barrage.common.MessageVersion;

/**
 * 服务端读取配置
 * @author jogeen
 * @version 1.0
 * @date 2018年10月14日
 */
public class ServerConfig {
	
	public static  Integer PORT = 7080;
	/**
	 * 窗口高度
	 */
	public static int WINDOW_HEIGHT;
	/**
	 * 窗口宽度
	 */
	public static int WINDOW_WIDTH;
	/**
	 * 弹幕移动速度
	 */
	public static int SPEED = 1;
	/**
	 * 刷新显示间隔
	 */
	public static int PERIOD = 5;
	/**
	 * 屏幕同时显示的弹幕数量
	 */
	public static int MAX_SCREEN_BARRAGE_COUNT = 3;
	
	/**
	 * 
	 */
	public static String  MINIMUM_VERSION=MessageVersion.LAST_VERSION;
	
	
	

	/**
	 * 读取配置文件，初始化配置信息
	 */
	public static void initConfig() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		WINDOW_HEIGHT = 300;
		WINDOW_WIDTH = (int) screenSize.getWidth();
		
		
		Properties properties = new Properties();
		// 使用ClassLoader加载properties配置文件生成对应的输入流
		InputStream in = ClientMain.class.getClassLoader().getResourceAsStream("config/server_config.properties");
		// 使用properties对象加载输入流
		try {
			properties.load(in);
		} catch (IOException e1) {
			throw new IllegalAccessError("配置文件读取失败");
		}
		
		// 获取key对应的value值
		String speed = properties.getProperty("speed");
		if (speed != null &&! "".equals(speed)) {
			SPEED = Integer.valueOf(speed);;
		}
		
		String period = properties.getProperty("period");
		if (period != null &&! "".equals(period)) {
			PERIOD = Integer.valueOf(period);;
		}
		
		String maxScreenBarrageCount = properties.getProperty("maxScreenBarrageCount");
		if (period != null &&! "".equals(maxScreenBarrageCount)) {
			MAX_SCREEN_BARRAGE_COUNT = Integer.valueOf(maxScreenBarrageCount);;
		}
		
		String bind_port = properties.getProperty("bindPort");
		if (bind_port != null &&! "".equals(bind_port)) {
			PORT = Integer.valueOf(bind_port);
		}
	}

}
