package client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import client.server.ClientFileService;
import client.server.ClientChatService;

public class ChatFrame extends JFrame {
    // 文本
    private JTextArea chatTxt = new JTextArea(); // 聊天框内文本
    private JTextArea inputTxt = new JTextArea(); // 聊天输入文本
    private JButton sendTxtButton = new JButton(); // 发送消息按钮
    // 文件
    private JTextField filePathTxt = new JTextField(); // 文件路径文本
    private JButton sendFileButton = new JButton(); // 发送文件按钮
    private JButton chooseFileButton = new JButton(); // 选择文件按钮
    private JProgressBar progressBar = new JProgressBar(); // 滚动条
    private String sendFileName = null; // 发送文件的文件名
    private ClientChatService clientChatService = null; // 聊天类
    private ClientFileService clientFileService = null; // 文件传输类
    //todo
    public ChatFrame(String userId, String getterId) {
        
    }

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
