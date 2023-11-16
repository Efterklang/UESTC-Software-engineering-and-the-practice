package server.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import common.Message;
import common.MessageType;
import common.User;
import server.utils.MyObjectInputStream;
import server.utils.MyObjectOutputStream;
import server.view.ServerFrame;
import server.utils.ProcessAccountData;

/**
 * @apiNote 服务器基本服务类，
 * @author gjx
 */
public class BasicService {
    private ServerSocket serverSocket = null;
    private ServerFrame serverFrame = null;
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();
    private final ProcessAccountData processAccountData = new ProcessAccountData();

    {
        validUsers.put("default", new User("default", "123456"));
        processAccountData.readAccountFile(validUsers);
    }

    public BasicService(ServerFrame serverFrame,Integer port) {
        this.serverFrame = serverFrame;
        
        try {
            serverSocket = new ServerSocket(port);
            InetAddress address = InetAddress.getLocalHost();
            printToServerFrame("服务器启动成功\nIp:" + address + "\nPort" + port + "监听中...");
            while (true) {
                // Listen for socket and receive User
                Socket socket = serverSocket.accept();
                MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
                MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
                
                // 接受客户端传来的User对象
                User user = (User) ois.readObject();
                // response to client
                Message responseToClient = new Message();

                if (user.getRegistMessageType() != null
                        && user.getRegistMessageType().equals(MessageType.REGIST_REQUEST)) {
                    if (isRegisterSucceed(user.getUserId(), user)) {
                        printToServerFrame("用户" + user.getUserId() + "注册成功");
                        responseToClient.setMessType(MessageType.REGIST_SUCCEED);
                        oos.writeObject(responseToClient);
                    } else {
                        printToServerFrame("用户" + user.getUserId() + "已存在, 注册失败");
                        responseToClient.setMessType(MessageType.REGIST_FAIL);
                        oos.writeObject(responseToClient);
                    }
                    user = null;
                    socket.close();
                } else if (user.getState() != null) {
                    ServerConnectThread thread = new ServerConnectThread(serverFrame, socket, user.getUserId());
                    thread.start();
                    ServerConnectThreadManage.addThread(user.getUserId(), user.getState(), thread);
                    if (user.getState() == "群聊") {
                        printToServerFrame("用户" + user.getUserId() + "创建群聊窗口");
                    } else {
                        printToServerFrame("用户" + user.getUserId() + "创建与" + user.getState() + "的私聊窗口");
                    }
                } else {
                    if (isLoginSucceed(user.getUserId(), user.getPassword())) {
                        printToServerFrame("用户" + user.getUserId() + "登录成功");
                        responseToClient.setMessType(MessageType.LOGIN_SUCCEED);
                        ServerConnectThread thread = new ServerConnectThread(serverFrame, socket, user.getUserId());
                        thread.start();
                        ServerConnectThreadManage.addThread(user.getUserId(), "在线", thread);
                        oos.writeObject(responseToClient);
                    } else {
                        printToServerFrame("用户" + user.getUserId() + "登录失败");
                        responseToClient.setMessType(MessageType.LOGIN_FAIL);
                        oos.writeObject(responseToClient);
                        socket.close();
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("BasicService.java: " + e.getMessage());
        } finally {
            
        }
    }

    private boolean isLoginSucceed(String userId, String pwd) {
        if (validUsers.get(userId) == null) {
            return false;
        } else {
            return validUsers.get(userId).getPassword().equals(pwd);
        }
    }

    private boolean isRegisterSucceed(String userId, User user) {
        if (validUsers.get(userId) == null) {
            processAccountData.writeAccountFile(userId, user.getPassword()); // 同步账号到数据库
            validUsers.put(userId, user);
            return true;
        } else {
            return false;
        }
    }

    public void printToServerFrame(String msg) {
        if (msg != null) {
            serverFrame.getShowcase().setText(serverFrame.getShowcase().getText() + msg + "\n");
        }
    }
}
