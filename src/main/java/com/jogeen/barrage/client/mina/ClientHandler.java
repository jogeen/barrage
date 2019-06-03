package com.jogeen.barrage.client.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.google.gson.Gson;
import com.jogeen.barrage.client.data.ClientDataCenter;
import com.jogeen.barrage.common.Message;
import com.jogeen.barrage.common.MessageDict;

/**
 * 
 * 客户端网络处理类
 * 
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class ClientHandler extends IoHandlerAdapter {
	
	
	
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		ClientDataCenter.appendMessage(MessageDict.SESSION_CREATED);
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		ClientDataCenter.appendMessage(MessageDict.SESSION_CLOSED);
	}
	@Override
	public void messageReceived(IoSession session, Object messageParam) throws Exception {
		String content =(String) messageParam;
		Gson gson=new Gson();
		if (content != null && !"".equals(content)) {
			Message message =null;
			try {
				message = gson.fromJson(content, Message.class);
			} catch (Exception e) {
			}
			if(message!=null) {
				ClientDataCenter.appendMessage(message.getMessageContent());
			} 
		}
	}
}
