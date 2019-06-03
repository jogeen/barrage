package com.jogeen.barrage.client;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.jogeen.barrage.client.config.ClientConfig;
import com.jogeen.barrage.client.data.ClientDataCenter;
import com.jogeen.barrage.client.frame.ClientFrame;
import com.jogeen.barrage.client.mina.ProtocolClient;

/**
 * 弹幕发送客户端
 * @author jogeen
 * @version 1.0
 *
 */
public class BarrageClient {
	
	public BarrageClient() {
		
	}
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
					System.out.println("开始启动客户端.......");
					ClientFrame frame = new ClientFrame();
					frame.setVisible(true);
					
					ClientDataCenter.appendMessage("开始读取配置.......");
					
					ClientConfig.initConfig();
					
					ClientDataCenter.appendMessage("连接服务器"+ClientConfig.SERVER_IP+":"+ClientConfig.PORT);
					
					ProtocolClient protocolClient = new ProtocolClient(ClientConfig.SERVER_IP,ClientConfig.PORT);
					
					protocolClient.startClient();
					
				} catch (Exception e) {
					e.printStackTrace();
					ClientDataCenter.appendMessage("连接服务器失败");
					JOptionPane.showMessageDialog(null, e.getMessage(), "启动失败", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
	}
	

}
