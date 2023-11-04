package client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.server.UserClientService;

/**
 * @apiNote ✨列表界面层
 * @author gjx
 * @version 1.0
 * @see client.server.UserClientService
 */
@SuppressWarnings("unused")
public class OnlineFriendsListFrame extends JFrame {
    private JButton getFriendListButton = new JButton();
    private JButton startChatButton = new JButton();
    private JLabel friendListLabel = new JLabel();
    private JTextArea showcase;
    private JTextField inputText,welcomeMsg;
    private Font fontYouYuan = new Font("youyuan", Font.BOLD, 17);
    private UserClientService userClientService;
    /**
     * @apiNote debug
     */
    public static void main(String[] args) {
        new OnlineFriendsListFrame("gjx", new UserClientService());
    }

    public OnlineFriendsListFrame(String userId, UserClientService userClientService) {
        this.userClientService = userClientService;
        userClientService.startListThread(userId, OnlineFriendsListFrame.this);

        // NorthPanel:拉取用户列表Button and Label
        getFriendListButton.setText("启动！");
        getFriendListButton.setFont(fontYouYuan);
        getFriendListButton.setBackground(Color.decode("#33a3dc"));
        friendListLabel.setText(" 点击查看列表=>");
        friendListLabel.setFont(fontYouYuan);
        friendListLabel.setBorder(BorderFactory.createLineBorder(Color.decode("#33a3dc"), 3));
        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.add(friendListLabel);
        northPanel.add(getFriendListButton);
        this.add(northPanel, BorderLayout.NORTH);
        // CenterPanel
        final JScrollPane scrollPane = new JScrollPane();
        welcomeMsg = new JTextField();
        welcomeMsg.setFont(new Font("Vladimir Script", Font.BOLD, 20));
        welcomeMsg.setText("        Hello!  Enjoy your chatting");
        welcomeMsg.setForeground(Color.decode("#2a5caa"));
        welcomeMsg.setBorder(BorderFactory.createLineBorder(Color.decode("#33a3dc"), 3));
        showcase = new JTextArea();
        showcase.setFont(fontYouYuan);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeMsg, BorderLayout.NORTH);
        panel.add(showcase, BorderLayout.CENTER);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(panel); 
        this.welcomeMsg.setEditable(false);
        this.showcase.setEditable(false);
        this.add(scrollPane, BorderLayout.CENTER);
        // SouthPanel:启动聊天Button and TextField
        JPanel southPanel = new JPanel(new FlowLayout());
        startChatButton.setText("进入聊天");
        startChatButton.setFont(fontYouYuan);
        startChatButton.setBackground(Color.decode("#33a3dc"));
        inputText = new JTextField(10);
        inputText.setFont(fontYouYuan);
        inputText.setBorder(BorderFactory.createLineBorder(Color.decode("#33a3dc"), 3));
        southPanel.add(inputText);
        southPanel.add(startChatButton);
        this.add(southPanel, BorderLayout.SOUTH);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // 电脑屏幕长宽
        this.setLocation((int) screenSize.getWidth() - 420, 0);
        this.setSize(350, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.setTitle("在线用户列表");

        /* 获取在线用户列表按钮监听 */
        getFriendListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                userClientService.getOnlineFriendList(userId);
            }
        });
        /* 开始聊天按钮监听 */
        startChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String getterId = inputText.getText();
                if (!getterId.equals("")) {
                    new ChatFrame(userId, getterId);
                    inputText.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "请输入对方用户名", "提示", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        /* 退出窗口事件监听 */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                JOptionPane.showMessageDialog(null, "确定退出吗", "关闭列表窗口", JOptionPane.QUESTION_MESSAGE);
                userClientService.logout(userId, "在线");
                System.exit(0);
            }
        });
    }

    /* getter setter */
    public JTextArea getShowcase() {
        return showcase;
    }

    public void setShowcase(JTextArea showcase) {
        this.showcase = showcase;
    }

    public JTextField getInputText() {
        return inputText;
    }

    public void setInputText(JTextField inputText) {
        this.inputText = inputText;
    }
}