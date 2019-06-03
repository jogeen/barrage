package com.jogeen.barrage.client.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.google.gson.Gson;
import com.jogeen.barrage.client.data.ClientDataCenter;
import com.jogeen.barrage.common.Message;



/**
 * 网络客户端
 * @author jogeen
 * @version 1.0
 *
 */
public class ProtocolClient {
	
	private InetSocketAddress socketAddress;
	
	private static Gson gson=new Gson();
	
	private static IoSession holdSession;
	
	
	public ProtocolClient(String serverIP,Integer post) {
		 socketAddress = new InetSocketAddress(
				 serverIP, post);
	}

	public void startClient() {
		IoConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		connector.getSessionConfig().setReadBufferSize(1024);
		connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);// 设置轮询间隔
		// 设置业务处理对象
		connector.setHandler(new ClientHandler());
		//连接服务
		ConnectFuture connectFuture = connector.connect(socketAddress);
		connectFuture.addListener(new IoFutureListener<ConnectFuture>() {
			public void operationComplete(ConnectFuture future) {
				if (future.isConnected()) {// 完成连接
					IoSession session = future.getSession();
					holdSession=session;
				}else {
					ClientDataCenter.appendMessage("连接服务器失败");
				}
			}
		});
	}

	/**
	 * 发送数据
	 * 
	 * @param session
	 */
	public static void sendData(Message message) {
		if(message!=null) {
			holdSession.write(gson.toJson(message));
		}
	}
}

