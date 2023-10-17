package client.view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author GaoJiaxing
 * @Version 1.0
 */

/*
 * Swing组件介绍
 * BackGroundInfo(JFrame组件):
 * JPanel：面板，可以在其中添加其他组件，用于组织和布局其他组件。
 * JLabel：标签，用于显示文本或图像。
 * JButton：按钮，用于触发事件。
 * JTextField：文本框，用于输入和显示文本。
 */
@SuppressWarnings("all")
public class LoginFrame extends JFrame {
        /* ① 顶部背景 */
        private JLabel background_North; // 顶部背景图片
        /* ② 登录栏 */
        private JPanel loginPanel; // 存放登录栏组件
        private JButton loginButton; // 存放登录按钮的图片
        /* ③ Center */
        private JTabbedPane choosePane;
        private JPanel centerPanel;

        private JLabel avatar;
        // 中部输入Panel
        private JPanel inputPanel;
        private JLabel userLabel, pwdLabel, emptyLabel;
        private JTextField userText; // 用户名输入
        private JPasswordField pwdTxt; // 密码输入
        private JButton clearButton, registerButton;

        public LoginFrame() {
                /* ① 顶部背景图片 */
        ImageIcon background = new ImageIcon();
        background.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/background.png"))
                .getScaledInstance(516, 170, Image.SCALE_DEFAULT));
        background_North = new JLabel(background);
        /* ② 登录栏 */
        loginPanel = new JPanel(); // 存放登录栏组件
        loginButton = new JButton(); // 存放登录按钮的图片
        /* 登录按钮图片 */
        ImageIcon loginIcon = new ImageIcon();
        loginIcon.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/loginButton.png"))
                .getScaledInstance(350, 45, Image.SCALE_DEFAULT));
        loginButton.setIcon(loginIcon);
        loginButton.setBorderPainted(false);
        loginButton.setBorder(null);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // 鼠标样式修改为手型，意为可以点击
        loginPanel.add(loginButton);
        /* ③ Center */
        // 3.1 Center-Left
        ImageIcon headIcon = new ImageIcon();
        headIcon.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/headshot.jpg"))
                        .getScaledInstance(120, 144, Image.SCALE_DEFAULT));
        avatar = new JLabel(headIcon);
        emptyLabel = new JLabel("              ");
        // 3.2.1 输入栏Panel
        inputPanel = new JPanel();
        centerPanel = new JPanel();
        centerPanel.add(avatar);
        centerPanel.add(emptyLabel);
        centerPanel.add(inputPanel);
        inputPanel.setSize(200, 300);
        choosePane = new JTabbedPane();
        choosePane.add("用户登录", centerPanel);
        
        // 3.2.2 输入栏组件
        userLabel = new JLabel("用户名", JLabel.CENTER);
        userLabel.setFont(new Font("youyuan", Font.BOLD, 16));
        pwdLabel = new JLabel("密  码", JLabel.CENTER);
        pwdLabel.setFont(new Font("youyuan", Font.BOLD, 16));
        userText = new JTextField();
        userText.setFont(new Font("youyuan", Font.PLAIN, 14));
        pwdTxt = new JPasswordField();
        // 3.2.3 输入栏组件清空 和 注册账号按钮
        clearButton = new JButton("清除号码");
        clearButton.setFont(new Font("youyuan", Font.PLAIN, 13));
        clearButton.setForeground(Color.RED);
        registerButton = new JButton("注册账号");
        registerButton.setFont(new Font("youyuan", Font.PLAIN, 13));
        registerButton.setForeground(Color.BLUE);
        registerButton.setPreferredSize(new Dimension(20, 20));
        // 3.2.4 输入栏布局 3*3网格
        inputPanel.setLayout(new GridLayout(3, 3));
        inputPanel.add(userLabel);
        inputPanel.add(userText);
        inputPanel.add(pwdLabel);
        inputPanel.add(pwdTxt);
        inputPanel.add(clearButton);
        inputPanel.add(registerButton);
        /* 设计界面布局 */
        add(background_North, BorderLayout.NORTH); // 顶部背景
        add(loginPanel, BorderLayout.SOUTH); // 底部登录栏
        add(choosePane, BorderLayout.CENTER); // 中部登录栏
        setVisible(true);
        setBounds(500, 150, 500, 460);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FreeChat_登录");
    }
}
