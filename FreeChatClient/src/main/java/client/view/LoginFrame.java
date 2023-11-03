package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;

import client.server.UserClientService;

import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @apiNote ✨登录界面层
 * @author gjx
 * @version 1.0
 * @see client.server.UserClientService
 */
public class LoginFrame extends JFrame {
	private JLabel background_North; // 顶部背景图片
	private JPanel loginPanel; // 存放登录栏组件
	private JButton loginButton; // 存放登录按钮的图片
	private JPanel centerPanel = new JPanel(); // 中部Panel
	private JLabel avatar; // 中部logo
	private JPanel inputPanel = new JPanel(); // 输入栏
	private JLabel userLabel, pwdLabel; // 用户名、密码
	private JTextField userText; // 用户名输入
	private JPasswordField pwdTxt; // 密码输入
	private JButton clearButton, registerButton; // 清空、注册
	// 业务逻辑(Login & Register)
	private UserClientService userClientService = null; // 用户登录注册类

	public LoginFrame() {
		/* 顶部背景图片 */
		ImageIcon background = new ImageIcon();
		background.setImage(
				Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/background.png"))
						.getScaledInstance(516, 170, Image.SCALE_DEFAULT));
		background_North = new JLabel(background);
		
		/* CenterPanel: avatar,空Panel(界面留白),inputPanel */
		// avatar
		ImageIcon avatarIcon = new ImageIcon();
		avatarIcon.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/headshot.jpg"))
				.getScaledInstance(140, 140, Image.SCALE_DEFAULT));
		avatar = new JLabel(avatarIcon);
		// inputPanel
		inputPanel.setSize(400, 300);
		inputPanel.setLayout(new GridLayout(5, 2));

		userLabel = new JLabel("用户名", JLabel.CENTER);
		userLabel.setFont(new Font("youyuan", Font.BOLD, 20));
		pwdLabel = new JLabel("密  码", JLabel.CENTER);
		pwdLabel.setFont(new Font("youyuan", Font.BOLD, 20));

		userText = new JTextField();
		userText.setFont(new Font("youyuan", Font.PLAIN, 20));
		pwdTxt = new JPasswordField();

		clearButton = new JButton("清除密码");
		clearButton.setFont(new Font("youyuan", Font.PLAIN, 15));
		clearButton.setForeground(Color.RED);
		registerButton = new JButton("注册账号");
		registerButton.setFont(new Font("youyuan", Font.PLAIN, 15));
		registerButton.setForeground(Color.BLUE);
		registerButton.setPreferredSize(new Dimension(20, 20));

		inputPanel.add(userLabel);
		inputPanel.add(userText);
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(pwdLabel);
		inputPanel.add(pwdTxt);
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(clearButton);
		inputPanel.add(registerButton);
		
		centerPanel.add(avatar);
		centerPanel.add(new JLabel("            "));
		centerPanel.add(inputPanel);

		/* 登录栏 */
		loginPanel = new JPanel();
		loginButton = new JButton();
		/* 登录按钮 */
		loginButton.setText("登录");
		loginButton.setFont(new Font("youyuan", Font.BOLD, 20));
		loginButton.setBackground(Color.decode("#33a3dc"));
		loginButton.setPreferredSize(new Dimension(220, 40));
		loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		loginButton.setBorderPainted(false);
		loginButton.setBorder(null);
		loginPanel.add(loginButton);

		/* 设计界面布局 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		add(background_North, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(loginPanel, BorderLayout.SOUTH);
		setVisible(true);
		setBounds(600, (int)(screenSize.getHeight()/2) - 315, 500, 430);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("FreeChat_登录");

		/* 添加监听 */
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String userId = userText.getText().trim();
				String pwd = new String(pwdTxt.getPassword()).trim();
				if ("".equals(userId) || userId == null) {
					JOptionPane.showMessageDialog(null, "请输入账号");
					return;
				}
				if ("".equals(pwd) || pwd == null) {
					JOptionPane.showMessageDialog(null, "请输入密码");
					return;
				}
				userClientService = new UserClientService();
				// 成功登录,关闭loginin窗口，打开userFrame窗口
				if (userClientService.checkUser(userId, pwd) == true) {
					LoginFrame.this.dispose();
					JOptionPane.showConfirmDialog(null, "登录成功");
					new OnlineFriendsListFrame(userId, userClientService);
				} else {
					JOptionPane.showMessageDialog(null, "账号或密码错误");
				}
			}
		});
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoginFrame.this.setVisible(false);
				new RegisterFrame();
			}
		});
		clearButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pwdTxt.setText("");
			}
		});
	}
}