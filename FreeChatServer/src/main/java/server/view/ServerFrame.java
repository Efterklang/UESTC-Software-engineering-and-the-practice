package server.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import server.service.BasicService;
import server.service.PushMessageService;

import javax.swing.JTextArea;

/**
 * @apiNote ✨服务端界面层
 * @author gjx
 * @version 1.0
 * @see server.service.BasicService
 * @see server.service.PushMessageService
 */
@SuppressWarnings("unused")
public class ServerFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private JButton startButton = new JButton("Run Server"); // 启动服务器
    private JButton pushButton = new JButton("Push Message"); // 推送信息按钮
    private JTextField pushTxt; // 需要推送的文本信息
    private JTextArea showcase; // 信息展示
    private Font fontYouYuan = new Font("youyuan", Font.BOLD, 16);
    private Color btnColor = Color.decode("#177cb0");
    private BasicService server;
    private PushMessageService pushMessageService;

    public static void main(String[] args) {
        new ServerFrame();
    }

    public ServerFrame() {
        super("FreeChat Server");
        // northPanel: pushTxt
        JPanel northPanel = new JPanel();
        northPanel.add(new JLabel("Push Message: "));
        pushTxt = new JTextField(40);
        pushTxt.setFont(fontYouYuan);
        pushTxt.setBorder(BorderFactory.createLineBorder(btnColor, 3));
        northPanel.add(pushTxt);
        this.add(northPanel, BorderLayout.NORTH);

        // centerPanel: showcase
        final JScrollPane scrollPane = new JScrollPane();
        showcase = new JTextArea();
        showcase.setFont(fontYouYuan);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setViewportView(showcase);
        this.showcase.setEditable(false);
        this.add(scrollPane, BorderLayout.CENTER);

        // southPanel: startButton, pushButton
        startButton.setFont(fontYouYuan);
        startButton.setBackground(btnColor);
        startButton.setBorderPainted(false);
        pushButton.setFont(fontYouYuan);
        pushButton.setBackground(btnColor);
        pushButton.setBorderPainted(false);
        JPanel southPanel = new JPanel();
        southPanel.add(startButton);
        southPanel.add(pushButton);
        this.add(southPanel, BorderLayout.SOUTH);

        // 设置窗口属性
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(500, 400);
        this.setLocation((int) screenSize.getWidth() / 2 - 250, (int) screenSize.getHeight() / 2 - 200);

        // 点击 "启动服务端" 按钮, 启动Server
        startButton.addActionListener(new ActionListener() {
            @Override
            /*
             * 启动服务端可能需要一些时间，此处使用单独的线程来启动服务端，防止阻塞。
             */
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String port = JOptionPane.showInputDialog("请输入开放的端口号：");
                new Thread() {
                    @Override
                    public void run() {
                        server = new BasicService(ServerFrame.this, Integer.parseInt(port));
                    }
                }.start();
            }
        });

        // 点击 "推送消息" 按钮, 向客户端推送消息
        pushButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String msg = pushTxt.getText();
                pushMessageService = new PushMessageService(ServerFrame.this);
                pushMessageService.pushMsg(msg);
                pushTxt.setText("");
            }
        });

        // 点击 "关闭" 按钮, 跳出确认窗口
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(null, "确认退出码", "Confirm", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    public void println(String s) {
        if (s != null) {
            this.showcase.setText(this.showcase.getText() + s + "\n");
        }
    }

    public JTextField getPushTxt() {
        return pushTxt;
    }

    public void setPushTxt(JTextField pushTxt) {
        this.pushTxt = pushTxt;
    }

    public JTextArea getShowcase() {
        return showcase;
    }

    public void setShowcase(JTextArea showcase) {
        this.showcase = showcase;
    }
}
