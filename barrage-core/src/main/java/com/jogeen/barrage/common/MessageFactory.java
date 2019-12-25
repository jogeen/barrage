package com.jogeen.barrage.common;


/**
 * 
 * 消息工程厂类
 * 
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class MessageFactory {


	public static Message createTextMessage(String content,String username) {
		return new Message(Message.TEXT_MESSAGE, content, MessageVersion.LAST_VERSION, username);
	}

	public static Message createGiftMessage(GiftEnum gift,String username) {
		return new Message(Message.IMAGE_MESSAGE, gift.getType(), MessageVersion.LAST_VERSION, username);
	}

	public static Message createSystemMessage(String content) {
		return new Message(Message.SYSTEM_MESSAGE, content, MessageVersion.LAST_VERSION, "system");
	}

}
