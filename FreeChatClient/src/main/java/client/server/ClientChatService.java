package client.server;

import client.view.ChatFrame;
import common.Message;
import common.MessageType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import client.utils.MyObjectOutputStream;

/**
 * @author gjx
 * @apiNote 客户端聊天服务，实现用户私聊，群聊功能
 */
public class ClientChatService {
    private ChatFrame chatFrame;
    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分ss秒");
    public ClientChatService(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
    }

    /**
     * @apiNote 将消息打印到聊天框
     * @param content 消息内容
     */
    public void printToChatFrame(String content) {
        if (content != null) {
            chatFrame.getChatTxt().setText(chatFrame.getChatTxt().getText() + content + "\n");
        }
    }

    /**
     * @apiNote 私聊方法
     * @param senderId 发送者id
     * @param getterId 接收者id
     * @param content  私聊发送内容
     */
    public void sendDirectMessage(String senderId, String getterId, String content) {
        // 构造message对象并初始化
        Message message = new Message();
        message.setMessType(MessageType.COMMON_MES);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());
        // 打印消息到聊天框
        printToChatFrame(senderId + "(我)" + sdf.format(new Date()));
        printToChatFrame(content + "\n");
        // 获取线程
        ClientConnectServerThread thread = ClientConnectServerThreadManage.getThread(senderId, getterId);
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * @apiNote 群聊方法
     * @param sendId 发送者id
     * @param content 群聊发送内容
     */
    public void sendGroupMessage(String sendId, String content) {
        Message message = new Message();
        message.setMessType(MessageType.GROUP_MES);
        message.setSender(sendId);
        message.setContent(content);
        message.setSendTime(new java.util.Date().toString());
        
        printToChatFrame(sendId + "(我)" + sdf.format(new Date()));
        printToChatFrame(content + "\n");

        ClientConnectServerThread thread = ClientConnectServerThreadManage.getThread(sendId, "群聊");
        try {
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
