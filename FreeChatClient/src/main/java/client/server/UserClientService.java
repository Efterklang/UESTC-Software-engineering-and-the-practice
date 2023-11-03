package client.server;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import javax.swing.JOptionPane;

import java.io.IOException;
import client.utils.MyObjectInputStream;
import client.utils.MyObjectOutputStream;
import client.view.ChatFrame;
import client.view.OnlineFriendsListFrame;
import common.Message;
import common.MessageType;
import common.User;

/**
 * @apiNote 客户端的用户服务类，实现功能如下
 * @checkUser 验证用户登录
 * @registerUser 进行用户的注册
 * @onlineFriendList 取在线用户列表
 * @logout 退出
 */
public class UserClientService {
    boolean status;
    private User user = new User();
    private Socket socket;

    /**
     * @apram userId 用户名
     * @param password 密码
     * @return 是否登录成功,true表示成功，false表示失败
     * @throws UnknownHostException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean checkUser(String userId, String password) {
        boolean status = false;
        try {
            user.setUserId(userId);
            user.setPassword(password);
            // 创建Socket,与服务器建立连接
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            // 发送User到服务端
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            // 接收服务端返回的Message
            MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if (message.getMessType().equals(MessageType.LOGIN_SUCCEED)) {
                status = true;
            } else {
                JOptionPane.showMessageDialog(null, "账号或密码错误");
                socket.close();
            }
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "服务器未启动");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "服务器未启动");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * @apram userId 用户名
     * @param password 密码
     * @return 是否注册成功,true表示成功，false表示失败
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public boolean registerUser(String userId, String password) {
        boolean status = false;
        try {
            user.setUserId(userId);
            user.setPassword(password);
            user.setRegistMessageType(MessageType.REGIST_REQUEST);
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
            Message message = (Message) ois.readObject();
            if (message.getMessType().equals(MessageType.REGIST_SUCCEED)) {
                JOptionPane.showMessageDialog(null, "注册成功");
                status = true;
            } else {
                JOptionPane.showMessageDialog(null, "注册失败");
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * @param senderId 发出拉取列表请求的用户的id
     * @throws IOException
     */
    public void onlineFriendList(String senderId) {
        Message message = new Message();
        message.setSendTime(new Date().toString());
        message.setSender(senderId);
        message.setMessType(MessageType.GET_ONLINE_FRIEND);

        try {
            ClientConnectServerThread thread = ClientConnectServerThreadManage.getThread(senderId, "在线");
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @apiNote 实现无异常退出
     * @param userId
     * @param state
     */
    public void logout(String userId, String state) {
        Message message = new Message();
        message.setSender(userId);
        message.setMessType(MessageType.CLIENT_EXIT);
        message.setGetter(state);
        // todo
    }

    /**
     * @apiNote 启动聊天窗口的线程
     * @param userId   用户名
     * @param state   状态，群聊/对方用户名
     * @param chatFrame
     */
    public void startChatThread(String userId, String state, ChatFrame chatFrame) {
        
    }

    /**
     * 使用Thread封装ClientService.socket和OnlineFriendsListFrame
     * 
     * @param userId
     * @param onlineUsersListFrame
     */
    public void startThread(String userId, OnlineFriendsListFrame onlineUsersListFrame) {
        ClientConnectServerThread clientConnectThread = new ClientConnectServerThread(socket, onlineUsersListFrame);
        clientConnectThread.start();
        ClientConnectServerThreadManage.addThread(userId, "在线", clientConnectThread);
    }
}
