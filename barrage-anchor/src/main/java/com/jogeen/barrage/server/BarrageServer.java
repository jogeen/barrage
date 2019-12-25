package com.jogeen.barrage.server;

import com.jogeen.barrage.server.config.ServerConfig;
import com.jogeen.barrage.server.frame.BarrageFrame;
import com.jogeen.barrage.server.mina.ProtocolServer;
import com.jogeen.barrage.server.mina.ServerHandler;

import java.io.IOException;

/**
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class BarrageServer {
	
	public BarrageServer() {
	}
	
	public void start() throws IOException {
		ServerConfig.initConfig();
    	BarrageFrame.getInstance(); 
		new ProtocolServer(ServerConfig.PORT,new ServerHandler()).startServer();
	}

}
