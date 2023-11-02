package client.server;

import client.utils.MyObjectOutputStream;
import client.view.ChatFrame;
import common.Message;
import common.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @apiNote 客户端文件传输类
 * @author gjx
 */
public class ClientFileService {
    private ChatFrame chatFrame = null;

    /**
     * @apiNote 将内容打印到聊天框
     * @param content 聊天内容
     */
    public void printToChatFrame(String content) {
        if (content != null) {
            chatFrame.getChatTxt().setText(chatFrame.getChatTxt().getText() + content + "\n");
        }
    }

    /**
     * @apiNote 发送文件给所有人
     * @param senderId  发送者id
     * @param getterId  接收者id
     * @param filePath  文件路径
     * @param fileName  文件名
     * @param chatFrame 聊天框
     */
    public void sendDirectFile(String senderId, String getterId, String filePath, String fileName,
            ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
        if (fileName == null) {
            return;
        }
        Message message = new Message();
        message.setMessType(MessageType.MESSAGE_FILE_MES);
        message.setFileName(fileName);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(new Date().toString());

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] fileBytes = new byte[(int) new File(filePath).length()];
            fis.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送给服务端
        try {
            ClientConnectThread thread = ClientConnectThreadManage.getThread(senderId, getterId);
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
            printToChatFrame(senderId + "(我)  发送文件:\t\t   " + new Date().toString());
            printToChatFrame("   路径为:  " + filePath + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @apiNote 发送文件到群聊
     * @param senderId  发送者id
     * @param filePath  文件路径
     * @param fileName  文件名
     * @param chatFrame 聊天框
     */
    public void sendGroupFile(String senderId, String filePath, String fileName, ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
        if (fileName == null) {
            return;
        }
        Message message = new Message();
        message.setMessType(MessageType.GROUP_FILE_MES);
        message.setSender(senderId);
        message.setSendTime(new Date().toString());
        message.setFileName(fileName);

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] fileBytes = new byte[(int) new File(filePath).length()];
            fis.read(fileBytes);
            message.setFileBytes(fileBytes);
            ClientConnectThread thread = ClientConnectThreadManage.getThread(senderId, "群聊");
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
            printToChatFrame(senderId + "(我)  发送文件:\t\t   " + new Date().toString());
            printToChatFrame("   路径为:  " + filePath + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}