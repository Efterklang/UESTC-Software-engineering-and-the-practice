package client.view;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Cursor;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * 客户端登录框架
 * 
 * 
 * BackGroundInfo(JFrame组件):
 * JMenuBar：菜单栏，可以在其中添加菜单和菜单项。
 * JToolBar：工具栏，可以在其中添加按钮和其他组件。
 * JPanel：面板，可以在其中添加其他组件，用于组织和布局其他组件。
 * JLabel：标签，用于显示文本或图像。
 * JButton：按钮，用于触发事件。
 * JTextField：文本框，用于输入和显示文本。
 * JTextArea：文本区域，用于输入和显示多行文本。
 * JScrollPane：滚动面板，用于显示带有滚动条的组件。
 * JComboBox：下拉列表框，用于选择一个选项。
 * JCheckBox：复选框，用于选择一个或多个选项。
 * JRadioButton：单选按钮，用于选择一个选项。
 * JTable：表格，用于显示和编辑表格数据。
 * JFileChooser：文件选择器，用于选择文件或目录
 */
public class LoginFrame extends JFrame {
    /*
     * Version 1.0: 
     *  绘制顶部背景图片＆底部登录栏
     *  Date: 2023.10.17
     *  Author: gjx          
     */
    /* 顶部背景 */
    private JLabel background_North;
    /* 底部登录栏 */
    private JPanel loginPanel;
    private JButton loginButton;
    
    public LoginFrame() {
        /* ①顶部背景图片 */
        ImageIcon background = new ImageIcon();
        background.setImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/background.png"))
                .getScaledInstance(516, 170, Image.SCALE_DEFAULT));
        background_North = new JLabel(background);
        /* ②登录栏 */
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
        /* ③Center */
        

        /* 设计界面布局 */
        add(background_North, BorderLayout.NORTH); // 顶部背景
        add(loginPanel, BorderLayout.SOUTH); // 底部登录栏
        setVisible(true);
        setBounds(500, 150, 500, 460);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("FreeChat_登录");
    }
}
