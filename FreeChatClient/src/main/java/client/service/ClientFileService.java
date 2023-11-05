package client.service;

import client.utils.MyObjectOutputStream;
import client.view.ChatFrame;
import common.Message;
import common.MessageType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author gjx
 * @apiNote 客户端文件传输类
 * <ul>
 * <li>sendGroupFile 群聊发送文件
 * <li>sendDirectFile 私聊发送文件
 * </ul>
 */
public class ClientFileService {
    private ChatFrame chatFrame = null;
    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分ss秒");
    
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
        message.setMessType(MessageType.DIRECT_FILE_MES);
        message.setFileName(fileName);
        message.setSender(senderId);
        message.setGetter(getterId);
        message.setSendTime(sdf.format(new Date()));

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] fileBytes = new byte[(int) new File(filePath).length()];
            fis.read(fileBytes);
            message.setFileBytes(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 发送给服务端
        try {
            ClientConnectServerThread thread = ClientConnectServerThreadManage.getThread(senderId, getterId);
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
            printToChatFrame(senderId + "(我) 发送文件:" + sdf.format(new Date()));
            printToChatFrame("路径为: " + filePath + "\n");
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
        message.setSendTime(sdf.format(new Date()));
        message.setFileName(fileName);

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] fileBytes = new byte[(int) new File(filePath).length()];
            fis.read(fileBytes);
            message.setFileBytes(fileBytes);
            ClientConnectServerThread thread = ClientConnectServerThreadManage.getThread(senderId, "群聊");
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
            printToChatFrame(senderId + "(我)  发送文件:  " + sdf.format(new Date()));
            printToChatFrame("路径为:  " + filePath + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
