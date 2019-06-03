package com.jogeen.barrage.common;

import com.jogeen.barrage.client.config.ClientConfig;
import com.jogeen.barrage.client.data.ClientDataCenter;

/**
 * 
 * 消息工程厂类
 * 
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class MessageFactory {

	private final static Integer TIME_INTERVAL = 5000;

	private static long LAST_CREATE_TIME = System.currentTimeMillis()-5000;

	public static Message createTextMessage(String content) {
		return new Message(Message.TEXT_MESSAGE, content, MessageVersion.LAST_VERSION, ClientConfig.NAME);
	}

	public static Message createGiftMessage(GiftEnum gift) {
		if(System.currentTimeMillis()-LAST_CREATE_TIME<TIME_INTERVAL) {
			ClientDataCenter.appendMessage("您的礼物发送太过频繁，稍等几秒，小心老师膨胀！");
			return null;
		}else {
			LAST_CREATE_TIME=System.currentTimeMillis();
			ClientDataCenter.appendMessage("送出"+gift.getName());
			return new Message(Message.IMAGE_MESSAGE, gift.getType(), MessageVersion.LAST_VERSION, ClientConfig.NAME);
		}
	}

	public static Message createSystemMessage(String content) {
		return new Message(Message.SYSTEM_MESSAGE, content, MessageVersion.LAST_VERSION, ClientConfig.NAME);
	}

}
