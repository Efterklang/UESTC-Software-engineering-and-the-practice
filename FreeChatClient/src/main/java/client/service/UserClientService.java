package client.service;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
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
 * @author gjx
 * @apiNote 客户端的用户服务类，一个User对应一个UserClientService Instance
 * <ul>
 * <li>checkUser 验证用户登录
 * <li>registerUser 进行用户的注册
 * <li>getOnlineFriendList 获取在线用户列表
 * <li>logout 退出
 * <li>startChatThread 启动聊天窗口的线程
 * <li>startListThread 启动在线用户列表的线程
 * </ul>
 */
public class UserClientService {
    boolean status;
    private User user = new User();
    private Socket socket;

    /**
     * @param userId 用户名
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
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "服务器未启动");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return status;
    }

    /**
     * @param userId 用户名
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
                status = true;
            } else {
                socket.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("UserClientService: registerUser: " + e.getMessage());
        }
        return status;
    }

    /**
     * @param senderId 发出请求用户的id
     * @throws IOException
     */
    public void getOnlineFriendList(String senderId) {
        Message message = new Message();
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日HH时mm分ss秒");
        message.setSendTime(sdf.format(new Date()));
        message.setSender(senderId);
        message.setMessType(MessageType.GET_ONLINE_FRIEND);

        try {
            // 线程管理类 的获取线程方法, 传入userId 和 状态"在线"
            ClientConnectServerThread thread = ClientConnectServerThreadManage.getThread(senderId, "在线");
            // 获取线程的socket, 并发送message
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @apiNote 实现无异常退出; 退出时向服务器发送退出消息，请求服务端关闭socket，退出线程
     * @param userId 用户名
     * @param state  状态，群聊/对方用户名(用于定位线程)
     */
    public void logout(String userId, String state) {
        Message message = new Message();
        message.setSender(userId);
        message.setMessType(MessageType.CLIENT_EXIT);
        message.setGetter(state);
        try {
            // 调用线程管理类，获取相应的线程
            ClientConnectServerThread thread = ClientConnectServerThreadManage.getThread(userId, state);
            // 发送message，请求退出
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @apiNote 启动聊天窗口的线程
     * @param userId   用户名
     * @param state   状态，群聊/对方用户名
     * @param chatFrame
     */
    public void startChatThread(String userId, String state, ChatFrame chatFrame) {
        user.setUserId(userId);
        user.setState(state);
        try {
            socket = new Socket(InetAddress.getLocalHost(), 9999);
            MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket, chatFrame);
        clientConnectServerThread.start();
        ClientConnectServerThreadManage.addThread(userId, state, clientConnectServerThread);
    }

    /**
     * @apiNote 启动在线用户列表的线程
     * @param userId
     * @param onlineUsersListFrame
     */
    public void startListThread(String userId, OnlineFriendsListFrame onlineUsersListFrame) {
        // 启动线程，用clientConnectServerThread封装维护socket
        // 将Thread放入线程管理类ClientConnectServerThreadManage
        ClientConnectServerThread clientConnectServerThread = new ClientConnectServerThread(socket,
                onlineUsersListFrame);
        clientConnectServerThread.start();
        ClientConnectServerThreadManage.addThread(userId, "在线", clientConnectServerThread);
    }
}
