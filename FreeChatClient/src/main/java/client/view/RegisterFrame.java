package client.view;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import client.service.UserClientService;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @apiNote ✨注册界面层
 * @author gjx
 * @version 1.0
 * @see client.service.UserClientService
 */
public class RegisterFrame extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JLabel background_North;
    private JLabel userLabel, pwdLabel, pwdLabel2; // 用户名，密码，二次输入密码
    private JButton registerButton; // 注册按钮
    private JTextField userText; // id输入框
    private JPasswordField pwdText, pwd2Text; // 密码,确认密码输入框
    private JPanel registerPanel, centerPanel;
    private JTabbedPane choose;

    public RegisterFrame() {
        /* ① 顶部背景图片 */
        ImageIcon background = new ImageIcon();
        background.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/background.png"))
                .getScaledInstance(516, 170, Image.SCALE_DEFAULT));
        background_North = new JLabel(background);
        /* ②注册栏 */
        registerPanel = new JPanel();
        /* 注册按钮 */
        registerButton = new JButton();
        registerButton.setText("注册");
        registerButton.setFont(new Font("youyuan", Font.BOLD, 20));
        registerButton.setBackground(Color.decode("#33a3dc"));
        registerButton.setPreferredSize(new Dimension(220, 40));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.setBorderPainted(false);
        registerPanel.add(registerButton);
        /* ③ 中心栏 */
        userLabel = new JLabel("用户名", JLabel.CENTER);
        userLabel.setFont(new Font("youyuan", Font.BOLD, 20));
        userText = new JTextField();
        userText.setFont(new Font("youyuan", Font.PLAIN, 20));
        pwdLabel = new JLabel("密   码", JLabel.CENTER);
        pwdLabel.setFont(new Font("youyuan", Font.BOLD, 20));
        pwdText = new JPasswordField();
        pwdLabel2 = new JLabel("确认密码", JLabel.CENTER);
        pwdLabel2.setFont(new Font("youyuan", Font.BOLD, 20));
        pwd2Text = new JPasswordField();
        centerPanel = new JPanel();
        choose = new JTabbedPane();
        choose.add("用户注册", centerPanel);
        centerPanel.setLayout(new GridLayout(7, 3));
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(userLabel);
        centerPanel.add(userText);
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());

        centerPanel.add(pwdLabel);
        centerPanel.add(pwdText);
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(pwdLabel2);
        centerPanel.add(pwd2Text);
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());
        centerPanel.add(new JLabel());

        // 设置页面布局
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        add(background_North, BorderLayout.NORTH);
        add(registerPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
        registerButton.addActionListener(this);
        setVisible(true);
        setResizable(false);
        setBounds(600, (int) (screenSize.getHeight() / 2) - 315, 500, 430);
        setTitle("FreeChat_注册");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String userId = userText.getText().trim();
            String pwd = new String(pwdText.getPassword()).trim();
            String pwd2 = new String(pwd2Text.getPassword()).trim();

            if ("".equals(userId) || userId == null) {
                JOptionPane.showMessageDialog(null, "请输入帐号");
                return;
            }
            if ("".equals(pwd) || pwd == null) {
                JOptionPane.showMessageDialog(null, "请输入密码");
                return;
            }
            if ("".equals(pwd2) || pwd2 == null) {
                JOptionPane.showMessageDialog(null, "请再次确认密码");
                return;
            }
            if (pwd.length() < 6) {
                JOptionPane.showMessageDialog(this, "密码长度小于6请重新输入");
                pwdText.setText("");
                pwd2Text.setText("");
                return;
            }
            if (!pwd2.equals(pwd)) {
                JOptionPane.showMessageDialog(null, "密码不一致，请重新输入");
                pwd2Text.setText("");
                return;
            }
            UserClientService clientService = new UserClientService();
            if (clientService.registerUser(userId, pwd)) {
                JOptionPane.showMessageDialog(this, "注册成功");
                this.setVisible(false);
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this,"注册失败,请重新注册");
            }
        }
    }
}
