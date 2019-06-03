package com.jogeen.barrage.client.frame;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Clock;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import com.jogeen.barrage.client.config.ClientConfig;
import com.jogeen.barrage.client.data.ClientDataCenter;
import com.jogeen.barrage.client.mina.ProtocolClient;
import com.jogeen.barrage.common.GiftEnum;
import com.jogeen.barrage.common.MessageFactory;
import com.jogeen.barrage.common.MessageVersion;

/**
 * 客户端应用类
 * 
 * @author jogeen
 * @version 1.0
 * @date 2018年10月18日
 */
public class ClientFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private static JTextArea textArea_record;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ClientFrame() {
		setTitle("666弹幕客户端"+MessageVersion.LAST_VERSION);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 328);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel.setBounds(0, 0, 580, 289);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 10, 283, 130);
		panel_2.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		final JTextArea textArea = new JTextArea(ClientConfig.COMMON_QUESTION[0]);
		textArea.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textArea.setBounds(10, 32, 263, 55);
		panel_2.add(textArea);
		
		JButton btnSend = new JButton("发送");
		btnSend.setBounds(180, 97, 93, 23);
		panel_2.add(btnSend);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = textArea.getText();
				// 向服务端发送消息
				if(text==null||"".equals(text)) {
					ClientDataCenter.appendMessage("发送弹幕不能为空");
					return;
				}
				ProtocolClient.sendData(MessageFactory.createTextMessage(text));
				ClientDataCenter.appendMessage(text);
			}
		});
		
		JLabel label = new JLabel("弹幕");
		label.setBounds(10, 10, 54, 15);
		panel_2.add(label);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(ClientConfig.COMMON_QUESTION));
		comboBox.setBounds(10, 97, 160, 21);
		panel_2.add(comboBox);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){
					textArea.setText((String)comboBox.getSelectedItem());
				}
			}
		});
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 164, 283, 103);
		panel_3.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JButton btnNewButton_2 = new JButton("火箭");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProtocolClient.sendData(MessageFactory.createGiftMessage(GiftEnum.ROCKET));
			}
		});
		btnNewButton_2.setBounds(24, 45, 63, 30);
		panel_3.add(btnNewButton_2);
		
		JButton button = new JButton("玫瑰");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProtocolClient.sendData(MessageFactory.createGiftMessage(GiftEnum.ROSE));
			}
		});
		button.setBounds(97, 45, 63, 30);
		panel_3.add(button);
		
		JLabel label_gift = new JLabel("礼物");
		label_gift.setBounds(10, 10, 54, 15);
		panel_3.add(label_gift);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(303, 10, 260, 257);
		panel.add(scrollPane);
		
		textArea_record = new JTextArea();
		scrollPane.setViewportView(textArea_record);
		textArea_record.setEditable(false);
		setTrayIcon();
		
		
	}

	private void setTrayIcon() {
		if (SystemTray.isSupported()) {// 判断系统是否托盘
			// 创建一个托盘图标对象
			try {
				TrayIcon icon = new TrayIcon(
						Toolkit.getDefaultToolkit().getImage(Clock.class.getResource("/image/main.png")));
				icon.setImageAutoSize(true);

				// 创建弹出菜单
				PopupMenu menu = new PopupMenu();
				// 添加一个用于退出的按钮
				MenuItem item = new MenuItem("exit");
				item.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				menu.add(item);
				// 添加弹出菜单到托盘图标
				icon.setPopupMenu(menu);
				SystemTray tray = SystemTray.getSystemTray();// 获取系统托盘
				try {
					tray.add(icon);
				} catch (AWTException e1) {
					e1.printStackTrace();
				} // 将托盘图表添加到系统托盘

				this.addWindowListener(new WindowAdapter() {
					public void windowIconified(WindowEvent e) {
						dispose();// 窗口最小化时dispose该窗口
					}
				});

				icon.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2)// 双击托盘窗口再现
							setExtendedState(Frame.NORMAL);
						setVisible(true);
					}
				});
			} catch (Exception e) {
				return;
			}
		}
	}
	
	public static void showRecord(String content) {
		if(textArea_record!=null) {
			textArea_record.setText(content);
		}
	}
}
