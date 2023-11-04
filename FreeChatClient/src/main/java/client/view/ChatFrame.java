package client.view;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.service.ClientChatService;
import client.service.ClientFileService;
import client.service.UserClientService;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.File;

import javax.swing.BorderFactory;

/**
 * @apiNote ✨聊天界面层，通过调用Service.clientChat/FileService完成功能
 * @author gjx
 * @version 1.0
 * @see client.service.ClientChatService
 * @see client.service.ClientFileService
 * @see client.service.UserClientService
 */
public class ChatFrame extends JFrame {
    // 文本
    private JTextArea chatTxt = new JTextArea(); // 聊天框内文本
    private JTextArea inputTxt = new JTextArea(); // 聊天输入文本
    private JButton sendTxtButton = new JButton(); // 发送消息按钮
    // 文件
    private JTextField filePathTxt = new JTextField(); // 文件路径文本
    private JButton sendFileButton = new JButton(); // 发送文件按钮
    private JButton chooseFileButton = new JButton(); // 选择文件按钮
    private JProgressBar progressBar = new JProgressBar(); // 进度条
    private String sendFileName = null; // 发送文件的文件名
    private ClientChatService clientChatService = null; // 聊天类
    private ClientFileService clientFileService = null; // 文件传输类
    private static final int FRAME_WIDTH = 660;
    private static final int FRAME_HEIGHT = 350;

    /**
     * @main 用于调试代码
     */
    public static void main(String[] args) {
        new ChatFrame("gjx", "Nancy");
    }

    public ChatFrame(String userId, String getterId) {
        this.clientFileService = new ClientFileService();
        this.clientChatService = new ClientChatService(ChatFrame.this);

        UserClientService userClientService = new UserClientService();
        userClientService.startChatThread(userId, getterId, ChatFrame.this);

        JScrollPane jScrollPane = new JScrollPane();// 滚动条
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setViewportView(this.chatTxt);
        jScrollPane.setBounds(1, 1, FRAME_WIDTH, FRAME_HEIGHT);
        this.add(jScrollPane);
        this.chatTxt.setEditable(false);
        chatTxt.setFont(new Font("youyuan", Font.BOLD, 16));
        chatTxt.setText(
                "\t\t\t**************************\n\t\t\t*    欢迎使用freechat     *\n\t\t\t**************************\n");
        // 输入面板 普通文本和文件路径文本
        inputTxt.setBounds(4, 355, FRAME_WIDTH - 6, 100); // 输入面板
        inputTxt.setFont(new Font("youyuan", Font.PLAIN, 12));
        inputTxt.setBorder(BorderFactory.createLineBorder(Color.decode("#33a3dc"), 3));
        filePathTxt.setFont(new Font("youyuan", Font.PLAIN, 12));
        filePathTxt.setText("若要传输文件，请单击选择文件:)");
        filePathTxt.setBounds(4, 470, FRAME_WIDTH - 6, 32);
        filePathTxt.setBorder(BorderFactory.createLineBorder(Color.decode("#33a3dc"), 3));
        this.add(filePathTxt);
        this.add(inputTxt);

        // 选择文件按钮
        chooseFileButton.setBounds(FRAME_WIDTH - 360, 520, 105, 35);
        chooseFileButton.setText("选择文件");
        chooseFileButton.setFont(new Font("youyuan", Font.BOLD, 16));
        chooseFileButton.setBackground(Color.decode("#33a3dc"));
        chooseFileButton.setPreferredSize(new Dimension(220, 40));
        this.add(chooseFileButton);
        // 发送文件按钮
        sendFileButton.setText("发送文件");
        sendFileButton.setFont(new Font("youyuan", Font.BOLD, 16));
        sendFileButton.setBackground(Color.decode("#33a3dc"));
        sendFileButton.setPreferredSize(new Dimension(220, 40));
        sendFileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendFileButton.setBounds(FRAME_WIDTH - 240, 520, 105, 35);
        sendFileButton.setBorderPainted(false);
        sendFileButton.setBorder(null);
        this.add(sendFileButton);
        // 发送消息按钮
        sendTxtButton.setText("发送消息");
        sendTxtButton.setFont(new Font("youyuan", Font.BOLD, 16));
        sendTxtButton.setBackground(Color.decode("#33a3dc"));
        sendTxtButton.setPreferredSize(new Dimension(220, 40));
        sendTxtButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendTxtButton.setBounds(FRAME_WIDTH - 120, 520, 105, 35);
        sendTxtButton.setBorderPainted(false);
        sendTxtButton.setBorder(null);
        this.add(sendTxtButton);
        // 进度条
        progressBar.setBounds(1, 560, 660, 20);
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setValue(0);

        this.add(progressBar);
        // 窗口布局
        setBounds(500, 100, 675, 600);
        setVisible(true);
        setResizable(false);

        if (getterId.equals("群聊")) {
            setTitle("群聊");
        } else {
            setTitle(userId + "与" + getterId + "的聊天窗口");
        }

        /* 选择文件按钮的事件监听 */
        chooseFileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    sendFileName = file.getName();
                    filePathTxt.setText(file.getAbsolutePath());
                }
            }
        });

        /* 发送文件按钮的事件监听 */
        sendFileButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (getterId.equals("群聊")) {
                    clientFileService.sendGroupFile(userId, filePathTxt.getText(), sendFileName, ChatFrame.this);
                } else {
                    clientFileService.sendDirectFile(userId, getterId, filePathTxt.getText(), sendFileName,
                            ChatFrame.this);
                }
                filePathTxt.setText("");
                sendFileName = null;
            }
        });

        /* 发送文本按钮的事件监听 */
        sendTxtButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (getterId.equals("群聊")) {
                    clientChatService.sendGroupMessage(userId, inputTxt.getText());
                    inputTxt.setText("");
                } else {
                    clientChatService.sendDirectMessage(userId, getterId, inputTxt.getText());
                    inputTxt.setText("");
                }
            }
        });
        /* 退出窗口事件监听 */
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                JOptionPane.showMessageDialog(null, "确定退出吗", "关闭聊天窗口", JOptionPane.QUESTION_MESSAGE);
                userClientService.logout(userId, getterId);
                this.setVisible(false);
            }

            private void setVisible(boolean b) {
            }
        });
        /* 点击文本框内容时，去除提示文本 */
        filePathTxt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                filePathTxt.setText("");
            }
        });
    }

    // getter and setter
    public JTextArea getChatTxt() {
        return chatTxt;
    }

    public void setChatTxt(JTextArea txt_Chat) {
        this.chatTxt = txt_Chat;
    }

    public JTextArea getInputTxt() {
        return inputTxt;
    }

    public void setInputTxt(JTextArea txt_Send) {
        this.inputTxt = txt_Send;
    }

    public JTextField getFilePathTxt() {
        return filePathTxt;
    }

    public void setFilePathTxt(JTextField txt_SendFile) {
        this.filePathTxt = txt_SendFile;
    }
}
