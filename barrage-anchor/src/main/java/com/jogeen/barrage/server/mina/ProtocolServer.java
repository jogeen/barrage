package com.jogeen.barrage.server.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 服务端
 * 
 * @author Tang
 *
 */
public class ProtocolServer {
	
	/**
	 * 绑定的IP地址
	 */
	//private static final String IP = "172.16.0.43";

	private IoHandlerAdapter handlerAdapter;

	private InetSocketAddress socketAddress ;
	
	public ProtocolServer(Integer port,IoHandlerAdapter handlerAdapter) {
		this.handlerAdapter=handlerAdapter;
		this.socketAddress=new InetSocketAddress(port);
	}

	public void startServer() throws IOException {
		IoAcceptor acceptor = new NioSocketAcceptor();
		// 设置过滤器，传入自己自定义的过滤器工厂
		acceptor.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new TextLineCodecFactory(Charset
						.forName("UTF-8"))));
		acceptor.getSessionConfig().setReadBufferSize(1024);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);//设置间隔时间
		// 设置业务处理对象
		acceptor.setHandler(handlerAdapter);
		// 绑定IP地址
		acceptor.bind(socketAddress);
		System.out.println("Server started");
	}
}
