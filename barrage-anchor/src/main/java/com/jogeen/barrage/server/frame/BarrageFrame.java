package com.jogeen.barrage.server.frame;

import com.jogeen.barrage.server.config.ServerConfig;
import com.jogeen.barrage.server.data.GiftImage;
import com.jogeen.barrage.server.data.ServerDataCenter;
import com.jogeen.barrage.server.model.GiftBarrage;
import com.jogeen.barrage.server.model.TextBarrage;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * 弹幕显示框架类
 * 
 * @author jogeen
 * @version 1.0
 *
 */
@SuppressWarnings("restriction")
public class BarrageFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private static BarrageFrame instacne = null;
	private static Semaphore semaphore = new Semaphore(0);

	private BarrageFrame() {
		Rectangle screenSize = new Rectangle(ServerConfig.WINDOW_WIDTH, ServerConfig.WINDOW_HEIGHT);
		Rectangle bounds = new Rectangle(screenSize);
		this.setTitle("发送弹幕");
		this.setLayout(null);
		this.setBounds(bounds);
		this.setExtendedState(Frame.MAXIMIZED_BOTH);
		this.setAlwaysOnTop(true);
		// 判断是否支持透明度
		this.setUndecorated(true); // 禁用或启用此窗体的修饰。只有在窗体不可显示时
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		AWTUtilities.setWindowOpaque(this, false);
		this.setVisible(true);

	}
	
	public static synchronized BarrageFrame getInstance() {
		if (instacne == null) {
			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						instacne = new BarrageFrame();
						instacne.paintBarrage();
						// 初始化完弹幕的Frame，释放信号
						semaphore.release();
					}
				});
				// 等待信号量的释放
				semaphore.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return instacne;
		}
		return instacne;
	}

	// 动画逻辑
	private void paintBarrage() {
		Timer textBarrageTimer = new Timer();
		textBarrageTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				moveTextBarrage();
				moveGiftBarrage();
				repaint();
			}
		}, 100, ServerConfig.PERIOD);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		paintTextBarrage(g);
		paintGiftBarrage(g);
		
	}
	
	private void moveTextBarrage() {
		List<TextBarrage> list = ServerDataCenter.TEXT_BARRAGE_LIST;
		int maxScreenBarrageCount = ServerConfig.MAX_SCREEN_BARRAGE_COUNT;
		// 限制屏幕同时显示的弹幕数量
		int index = 0;
		if (list.size() > maxScreenBarrageCount)
			index = list.size() - maxScreenBarrageCount;
		for (int i = index; i < list.size(); i++) {
			TextBarrage barrage = list.get(i);
			barrage.x -= barrage.speed;
		}
	}
	
	private void moveGiftBarrage() {
		List<GiftBarrage> list = ServerDataCenter.GIFT_BARRAGE_LIST;
		int maxScreenBarrageCount = ServerConfig.MAX_SCREEN_BARRAGE_COUNT;
		// 限制屏幕同时显示的弹幕数量
		int index = 0;
		if (list.size() > maxScreenBarrageCount)
			index = list.size() - maxScreenBarrageCount;
		for (int i = index; i < list.size(); i++) {
			GiftBarrage barrage = list.get(i);
			barrage.x -= barrage.speed*2;
		}
	}

	private void paintTextBarrage(Graphics g) {
		List<TextBarrage> list = ServerDataCenter.TEXT_BARRAGE_LIST;
		int maxScreenBarrageCount = ServerConfig.MAX_SCREEN_BARRAGE_COUNT;
		// 限制屏幕同时显示的弹幕数量
		int index = 0;
		if (list.size() > maxScreenBarrageCount) {
			index = list.size() - maxScreenBarrageCount;
		}
		for (int i = index; i < list.size(); i++) {
			TextBarrage text = ServerDataCenter.TEXT_BARRAGE_LIST.get(i);
			if (text.x < -ServerConfig.WINDOW_WIDTH/2) {
				// 显示结束，移除列表
				ServerDataCenter.TEXT_BARRAGE_LIST.remove(i);
				continue;
			} else {
				g.setColor(text.getColor());
				g.setFont(new Font("楷体", text.getStyle(), text.getSize()));
				g.drawString(text.getText(), text.x, text.y);
			}
		}
	}

	private void paintGiftBarrage(Graphics g) {
		List<GiftBarrage> list = ServerDataCenter.GIFT_BARRAGE_LIST;
		int maxScreenBarrageCount = ServerConfig.MAX_SCREEN_BARRAGE_COUNT;
		// 限制屏幕同时显示的弹幕数量
		int index = 0;
		if (list.size() > maxScreenBarrageCount) {
			index = list.size() - maxScreenBarrageCount;
		}
		for (int i = index; i < ServerDataCenter.GIFT_BARRAGE_LIST.size(); i++) {
			GiftBarrage giftBarrage = ServerDataCenter.GIFT_BARRAGE_LIST.get(i);
			if (giftBarrage.x < -ServerConfig.WINDOW_WIDTH/2) {
				// 显示结束，移除列表
				ServerDataCenter.GIFT_BARRAGE_LIST.remove(i);
				continue;
			} else {
				BufferedImage img = GiftImage.getImageByName(giftBarrage.getImageName());
				g.drawImage(img, giftBarrage.x, giftBarrage.y, img.getWidth(), img.getHeight(), this);
				
				g.setColor(giftBarrage.getColor());
				g.setFont(new Font("楷体", 1, 40));
				g.drawString(giftBarrage.getThanksMessage(), giftBarrage.x+img.getWidth(), giftBarrage.y+img.getHeight());
			}
		}

	}

}
