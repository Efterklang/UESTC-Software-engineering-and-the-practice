package client.server;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import java.io.IOException;
import client.utils.MyObjectInputStream;
import client.utils.MyObjectOutputStream;
import common.Message;
import common.User;

public class clientService {
    boolean status = false;
    private User user = new User();
    private Socket socket;

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
            if (message.getMessType().equals(common.MessageType.MESSAGE_LOGIN_SUCCEED)) {
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

    public boolean registerUser(String userId, String password) {
        boolean status = false;
        return status;
    }
}
