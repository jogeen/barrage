package com.jogeen.barrage.web.mina;

import com.google.gson.Gson;
import com.jogeen.barrage.common.Message;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.stereotype.Component;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;


/**
 * 网络客户端
 * @author jogeen
 * @version 1.0
 *
 */
@Component
public class ProtocolClient {
	
	private InetSocketAddress socketAddress;
	
	private static Gson gson=new Gson();
	
	private  IoSession holdSession;
	
	
	public ProtocolClient() {
	}

	public void initClient(String ip,Integer port) {
		if(holdSession!=null){
			holdSession.closeNow();
		}
		socketAddress = new InetSocketAddress(
				ip, port);
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
					System.out.println("链接失败");
				}
			}
		});
	}

	/**
	 * 发送数据
	 * 
	 * @param message
	 */
	public String sendData(Message message) {
		if(holdSession==null){
			return "发送失败，请联系管理员连接主播屏";
		}
		if(message!=null) {
			holdSession.write(gson.toJson(message));
		}
		return "发送成功";
	}
}

