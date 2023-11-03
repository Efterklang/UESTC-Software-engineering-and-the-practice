package client.view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import client.server.ClientFileService;
import client.server.UserClientService;
import client.server.ClientChatService;
import java.awt.Font;
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
        System.out.println(Thread.currentThread().getName());
        this.clientFileService = new ClientFileService();
        this.clientChatService = new ClientChatService(ChatFrame.this);
        UserClientService clientService = new UserClientService();
        clientService.startChatThread(userId, getterId, ChatFrame.this);

        JScrollPane jScrollPane = new JScrollPane();// 滚动条
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setViewportView(this.chatTxt);
        jScrollPane.setBounds(1, 1, 660, 350);
        this.add(jScrollPane);
        this.chatTxt.setEditable(false);
        chatTxt.setFont(new Font("youyuan", Font.BOLD, 16));
        chatTxt.setText(
                "\t\t\t**************************\n\t\t\t*    欢迎使用freechat     *\n\t\t\t**************************\n");
        setBounds(500, 100, 675, 600);
        setVisible(true);
        setResizable(false);
        if (getterId.equals("群聊")) {
            setTitle("群聊");
        } else {
            setTitle(userId + "与" + getterId + "的聊天窗口");
        }
    }

    public static void main(String[] args) {
        new ChatFrame("gjx", "abc");
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
