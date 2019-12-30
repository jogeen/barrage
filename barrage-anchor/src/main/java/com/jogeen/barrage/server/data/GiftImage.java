package com.jogeen.barrage.server.data;

import com.jogeen.barrage.common.GiftEnum;
import com.jogeen.barrage.server.ServerMain;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *  礼物图片加载类
 * @author jogeen
 * @version 1.0
 * @date 2018年10月16日
 */
public class GiftImage {
	private static String path;

	static {
		try {
			path = java.net.URLDecoder.decode(ServerMain.class.getResource("/").getPath(),"utf-8");
			System.out.println(path);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static Map<String,BufferedImage> imgMap=new HashMap();

	public static BufferedImage getImageByName(String imageName) {
		BufferedImage bufferedImage = imgMap.get(imageName);
		if(bufferedImage!=null){
			return bufferedImage;
		}else{
			GiftEnum giftEnum = GiftEnum.getGiftByType(imageName);
			if(giftEnum!=null){
				try {
					BufferedImage image = ImageIO.read(new FileInputStream(path + "/image/"+giftEnum.getType()+".png"));
					imgMap.put(imageName,image);
					return image;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
