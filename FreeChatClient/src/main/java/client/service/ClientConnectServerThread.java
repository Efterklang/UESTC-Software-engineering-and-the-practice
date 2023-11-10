package client.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import client.utils.MyObjectInputStream;
import client.view.ChatFrame;
import client.view.OnlineFriendsListFrame;
import common.Message;
import common.MessageType;

/**
 * @apiNote 客户端线程类，进行客户端与服务端的通信，持有并维护socket
 * @author gjx
 * @version 1.0
 * @run 客户端不停接收来自服务端的Message对象<code>while(true)</code>，根据不同的MessageType进行不同的操作:
 * <ul>     
 * <li>在线用户列表 
 * <li>私聊内容 
 * <li>群聊内容 
 * <li>私发文件
 * <li>群发文件
 * <li>退出,关闭socket<
 * </ul>
 */
public class ClientConnectServerThread extends Thread {
    private Socket socket;
    private ChatFrame chatFrame;
    private OnlineFriendsListFrame onlineFriendsListFrame;

    public ClientConnectServerThread(Socket socket, ChatFrame chatFrame) {
        super();
        this.socket = socket;
        this.chatFrame = chatFrame;
    }

    public ClientConnectServerThread(Socket socket, OnlineFriendsListFrame onlineFriendsListFrame) {
        super();
        this.socket = socket;
        this.onlineFriendsListFrame = onlineFriendsListFrame;
    }

    @Override
    public void run() {
        while (true) {
            try {
                MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
                Message message = (Message) ois.readObject();

                switch (message.getMessType()) {
                    // 返回在线用户列表(message.getContent())
                    case MessageType.RETURN_ONLINE_FRIEND ->
                            printToListFrame(message.getSendTime() + "\n========== 在线用户 =========\n"
                                    + "群聊\n" + message.getContent());

                    // 接收普通私聊消息和群聊消息
                    case MessageType.COMMON_MES, MessageType.GROUP_MES -> {
                        printToChatFrame(message.getSender() + "\t\t\t" + message.getSendTime());
                        printToChatFrame(message.getContent() + "\n");
                    }
                    // 接收文件消息
                    case MessageType.DIRECT_FILE_MES, MessageType.GROUP_FILE_MES -> {
                        printToChatFrame(message.getSender() + "\t\t\t" + message.getSendTime());
                        printToChatFrame(message.getFileName() + "\n");
                        String filePath = saveFileAddress(message.getGetter(), message.getSender(),
                                message.getFileName());
                        if (filePath != null) {
                            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                                fos.write(message.getFileBytes());
                                JOptionPane.showMessageDialog(null, "保存成功", "Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                            } catch (FileNotFoundException e) {
                                JOptionPane.showMessageDialog(null, "保存失败，文件路径错误", "Warning",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                    case MessageType.CLIENT_EXIT -> socket.close();
                    default -> {
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("ClientConnectServerThread: run: " + e.getMessage());
            }
        }
    }

    /**
     * @apiNote 文件接受窗口，保存文件地址
     * @param userId   用户名
     * @param sender   文件的发送者
     * @param fileName 文件名
     * @return filePath 保存到本地的绝对路径
     */
    public String saveFileAddress(String userId, String sender, String fileName) {
        return JOptionPane.showInputDialog(null,
                sender + "发送了" + fileName + "\n 请输入您想保存的路径(绝对路径):\ne.g:E:\\UESTC\\fileName.md",
                "文件接收",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * @apiNote 打印消息到聊天窗口的chatTxt
     * @param content 消息内容
     */
    public void printToChatFrame(String content) {
        if (content != null) {
            chatFrame.getChatTxt().setText(chatFrame.getChatTxt().getText() + content + "\n");
        }
    }

    /**
     * @apiNote 打印在线用户到在线用户窗口showcase
     * @param friends 在线用户列表
     */
    public void printToListFrame(String friends) {
        if (friends != null) {
            onlineFriendsListFrame.getShowcase().setText(friends + "\n");
        }
    }

    /* setter getter */
    public Socket getSocket() {
        return socket;
    }

}
