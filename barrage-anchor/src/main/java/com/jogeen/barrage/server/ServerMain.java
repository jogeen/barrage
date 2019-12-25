package com.jogeen.barrage.server;

import java.io.IOException;

/**
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class ServerMain {
	public static void main(String[] args) throws IOException {  
		new BarrageServer().start();
    }
}
