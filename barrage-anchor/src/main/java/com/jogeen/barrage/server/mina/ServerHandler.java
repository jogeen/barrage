package com.jogeen.barrage.server.mina;

import com.google.gson.Gson;
import com.jogeen.barrage.common.Message;
import com.jogeen.barrage.common.MessageFactory;
import com.jogeen.barrage.server.config.ServerConfig;
import com.jogeen.barrage.server.data.ServerDataCenter;
import com.jogeen.barrage.server.model.GiftBarrage;
import com.jogeen.barrage.server.model.TextBarrage;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ServerHandler extends IoHandlerAdapter {
	
	private Integer onlineClientCount=0;
	
	
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		onlineClientCount--;
		System.out.println("有客户端"+session.getRemoteAddress().toString()+"下线!");
		System.out.println("当前在线用户数:"+onlineClientCount);
	}
	

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		System.out.println("有客户端"+session.getRemoteAddress().toString()+"成功连接服务器!");
		onlineClientCount++;
		System.out.println("当前在线用户数:"+onlineClientCount);
		
	}

	@Override
	public void messageReceived(IoSession session, Object messageParam) throws Exception {
		String content =(String) messageParam;
		System.out.println("服务器端接收到来来之IP"+session.getRemoteAddress()+"数据:" + content);
		Gson gson=new Gson();
		if (content != null && !"".equals(content)) {
			Message message =null;
			try {
				message = gson.fromJson(content, Message.class);
			} catch (Exception e) {
			}
			
			
			if(!checkVersion(message)){
				session.write(gson.toJson(MessageFactory.createSystemMessage("客户端版本过低!当前要求最低版本为"+ServerConfig.MINIMUM_VERSION)));
				return;
			}
			if(Message.TEXT_MESSAGE.equals(message.getType())) {
				
				TextBarrage textBarrage = new TextBarrage(message.getSender(),message.getMessageContent());
				
				
				ServerDataCenter.add(textBarrage);
			}else if(Message.IMAGE_MESSAGE.equals(message.getType())){
				
				
				GiftBarrage giftBarrage = new GiftBarrage(message.getSender(),message.getMessageContent());
				
				ServerDataCenter.add(giftBarrage);
			}else {
				session.write("无效的消息类型");
			}
		}
	}


	private boolean checkVersion(Message message) {
		String version = message.getVersion();
		if(version==null||"".equals(version)) {
			return false;
		}
		Double clientVersion;
		try {
			clientVersion = Double.valueOf(version);
		} catch (Exception e) {
			return false;
		}
		if(clientVersion>=Double.valueOf(ServerConfig.MINIMUM_VERSION)) {
			return true;
		}
		return false;
	}
	
}
