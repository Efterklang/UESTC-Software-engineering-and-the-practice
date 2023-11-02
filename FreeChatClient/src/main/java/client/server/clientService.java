package client.server;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import javax.swing.JOptionPane;

import java.io.IOException;
import client.utils.MyObjectInputStream;
import client.utils.MyObjectOutputStream;
import client.view.OnlineFriendsListFrame;
import common.Message;
import common.MessageType;
import common.User;

/*
 * 客户端 用户服务类 
 * Function:
 *      1. 用户登录验证 public boolean checkUser()
 *      2. 用户注册验证 public boolean registerUser()
 *      3. 获取在线用户列表 public void onlineFriendList()
 *      4. 无异常退出 public void logout()
 *      5. 线程管理 public boolean startThread() and
 *                 public void startChatThread()
 */
public class ClientService {
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
            ClientConnectThread thread = ClientConnectThreadManage.getThread(senderId, "在线");
            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 实现无异常退出
     * 
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
     * 使用Thread封装ClientService.socket和OnlineFriendsListFrame
     * 
     * @param userId
     * @param onlineUsersListFrame
     */
    public void startThread(String userId, OnlineFriendsListFrame onlineUsersListFrame) {
        ClientConnectThread clientConnectThread = new ClientConnectThread(socket, onlineUsersListFrame);
        clientConnectThread.start();
        ClientConnectThreadManage.addThread(userId, "在线", clientConnectThread);
    }

}
