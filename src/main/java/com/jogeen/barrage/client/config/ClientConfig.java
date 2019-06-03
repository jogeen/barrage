package com.jogeen.barrage.client.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.jogeen.barrage.client.ClientMain;

/**
 * 客户端配置类
 * 该类主要是是读取和保存客户端的配置文件信息
 * 
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class ClientConfig {
	public static String SERVER_IP="127.0.0.1";
	public static Integer PORT=7080;
	public static String SAY_HELLO="hello!";
	public static String NAME="";
	public static String[] COMMON_QUESTION= {"666","森瑟，为何你这么帅？", "谁能告诉我？","森瑟，可以下课休息了"};
	
	
	public static void initConfig() {
		Properties properties = new Properties();
	    // 使用ClassLoader加载properties配置文件生成对应的输入流
	    InputStream in = ClientMain.class.getClassLoader().getResourceAsStream("config/client_config.properties");
	    // 使用properties对象加载输入流
	    try {
			properties.load(in);
		} catch (IOException e1) {
			throw new IllegalAccessError("配置文件读取失败");
		}
	    //获取key对应的value值
	    String server_ip=properties.getProperty("server_ip");
	    if(server_ip==null||"".equals(server_ip)) {
	    	throw new IllegalAccessError("服务器IP为空");
	    }
	    SERVER_IP=server_ip;
	    
	    String port=properties.getProperty("port");
	    if(port==null||"".equals(port)) {
	    	throw new IllegalAccessError("服务器端口为空");
	    }
	    PORT=Integer.valueOf(port);
	    
	    String say_hello=properties.getProperty("say_hello");
	    if(say_hello!=null&&!"".equals(say_hello)) {
	    	SAY_HELLO=say_hello;
	    }
	    
	    String name=properties.getProperty("name");
	    if(name!=null&&!"".equals(name)) {
	    	NAME=name;
	    }else {
	    	NAME=System.getProperty("user.name");
	    }
	
	}
	
	
	
}
