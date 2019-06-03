package com.jogeen.barrage.server.data;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import com.jogeen.barrage.common.GiftEnum;

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
			path = java.net.URLDecoder.decode(GiftImage.class.getResource("/").getPath(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage roseImg = null;
	private static BufferedImage rocketImg = null;

	public static BufferedImage getRoseImage() {
		if (roseImg == null) {
			try {
				roseImg = ImageIO.read(new FileInputStream(path + "/image/rose.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return roseImg;
	}

	public static BufferedImage getRocketImage() {
		if (rocketImg == null) {
			try {
				rocketImg = ImageIO.read(new FileInputStream(path + "/image/rocket.png"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rocketImg;
	}
	
	public static BufferedImage getImageByName(String imageName) {
		if(GiftEnum.ROSE.getType().equals(imageName)) {
			return getRoseImage();
		}
		if(GiftEnum.ROCKET.getType().equals(imageName)) {
			return getRocketImage();
		}
		return null;
	}

}
