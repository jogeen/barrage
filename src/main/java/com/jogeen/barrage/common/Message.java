package com.jogeen.barrage.common;

/**
 * 消息类
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class Message {
	
	public final static String TEXT_MESSAGE="text";
	public final static String IMAGE_MESSAGE="image";
	public final static String SYSTEM_MESSAGE="system";
	
	private String type;
	private String messageContent;
	private String version;
	private String sender;
	
	
	public Message() {
	}
	
	public Message(String type, String messageContent, String version) {
		super();
		this.type = type;
		this.messageContent = messageContent;
		this.version = version;
	}
	
	


	public Message(String type, String messageContent, String version, String sender) {
		super();
		this.type = type;
		this.messageContent = messageContent;
		this.version = version;
		this.sender = sender;
	}

	public Message(String type, String messageContent) {
		this.type = type;
		this.messageContent = messageContent;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	
}
