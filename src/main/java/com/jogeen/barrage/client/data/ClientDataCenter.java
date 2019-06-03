package com.jogeen.barrage.client.data;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jogeen.barrage.client.frame.ClientFrame;

/**
 * 客户端数据中心类
 * 该类主要保存客户端消息数据
 * 
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class ClientDataCenter {
	
	private static StringBuffer MESSAGE_RECORD=new StringBuffer();
	
	public static void appendMessage(String content) {
		SimpleDateFormat sf=new SimpleDateFormat("HH:mm:ss");
		String time = sf.format(new Date());
		MESSAGE_RECORD.append("\r\n").append(time).append("\r\n").append(content);
		ClientFrame.showRecord(MESSAGE_RECORD.toString());
		
		
	}
}
