package server.service;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import common.Message;
import common.MessageType;
import server.utils.MyObjectOutputStream;
import server.utils.MyObjectInputStream;
import server.view.ServerFrame;

/**
 * @author gjx
 * @apiNote 服务器连接线程类
 * @version 1.0
 */
public class ServerConnectThread extends Thread {
    private ServerFrame serverFrame = null;
    private Socket socket;
    private String userId;

    public ServerConnectThread(ServerFrame serverFrame, Socket socket, String userId) {
        super();
        this.serverFrame = serverFrame;
        this.socket = socket;
        this.userId = userId;
    }

    /**
     * @apiNote 不停接收读取客户端发送的数据, 保持通信；根据消息类型进行不同的处理
     *          <ul>
     *          <li>1.接收拉取在线用户请求, 返回message给原用户</li>
     *          <li>2.接收私聊请求, 发送信息包给指定用户</li>
     *          <li>3.接收群聊请求, 发送消息包给所有在线用户</li>
     *          <li>4.接收私发文件请求</li>
     *          <li>5.接收群发文件请求</li>
     *          <li>6.接收(线程)退出请求, 将信息发送给其线程提示退出, 并break退出该线程 关闭socket</li>
     *          <li>包括 聊天窗口(线程)退出 和 客户端退出</li>
     *          </ul>
     */
    @Override
    public void run() {
        while (true) {
            printToServerFrame(userId + " is connected.");
            try {
                MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
                // 若Client没有发送对象, 则程序在此阻塞(暂停)
                Message message = (Message) ois.readObject();
                String type = message.getMessType();
                if (type.equals(MessageType.GET_ONLINE_FRIEND)) {

                    String onlineUsers = ServerConnectThreadManage.getOnlineUsers();
                    Message responceToClinet = new Message();
                    responceToClinet.setMessType(MessageType.RETURN_ONLINE_FRIEND);
                    responceToClinet.setContent(onlineUsers);
                    responceToClinet.setSendTime(message.getSendTime());
                    responceToClinet.setSender(message.getSender());

                    printToServerFrame("Send online users List to " + userId + "...");
                    MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(responceToClinet);
                } else if (type.equals(MessageType.COMMON_MES)) {
                    ServerConnectThread thread = ServerConnectThreadManage.getThread(message.getGetter(),
                            message.getSender());

                    if (thread != null) { // 线程存在
                        MyObjectOutputStream oos2 = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                        oos2.writeObject(message);
                        printToServerFrame(
                                message.getGetter() + "accepted" + message.getSender() + "'s message successfully");
                    } else {
                        printToServerFrame(message.getSender() + "failed to find " + message.getGetter() + "!");
                    }
                } else if (type.equals(MessageType.GROUP_MES)) {
                    HashMap<String, HashMap<String, ServerConnectThread>> map = ServerConnectThreadManage.getMap();
                    Iterator<String> iterator = map.keySet().iterator();
                    String onlineUserId;

                    while (iterator.hasNext()) {
                        onlineUserId = iterator.next();
                        message.setGetter(onlineUserId);
                        ServerConnectThread thread = map.get(onlineUserId).get("群聊");
                        // 将信息发送给除了自己的其他在线用户
                        if (!onlineUserId.equals(message.getSender())) {
                            MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                            oos.writeObject(message);
                            printToServerFrame(
                                    onlineUserId + "accepted" + message.getSender() + "'s group message successfully");
                        } else {
                            printToServerFrame(
                                    onlineUserId + "failed to accept" + message.getSender() + "'s group message!");
                        }
                    }
                } else if (type.equals(MessageType.DIRECT_FILE_MES)) {
                    String getterId = message.getGetter();
                    ServerConnectThread thread = ServerConnectThreadManage.getThread(getterId, userId);
                    if (thread != null) {
                        MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                        oos.writeObject(message);
                        printToServerFrame(userId + "sent file to" + getterId + "successfully");
                    } else {
                        printToServerFrame(userId + "failed to find" + getterId + "!");
                    }
                } else if (type.equals(MessageType.GROUP_FILE_MES)) {
                    HashMap<String, HashMap<String, ServerConnectThread>> map = ServerConnectThreadManage.getMap();
                    Iterator<String> iterator = map.keySet().iterator();
                    String onlineUserId;
                    while (iterator.hasNext()) {
                        onlineUserId = iterator.next().toString();
                        if (!onlineUserId.equals(message.getSender())) {
                            message.setGetter(onlineUserId);
                            ServerConnectThread thread = map.get(onlineUserId).get("群聊");
                            if (thread != null) {
                                MyObjectOutputStream oos = new MyObjectOutputStream(
                                        thread.getSocket().getOutputStream());
                                oos.writeObject(message);
                                printToServerFrame(
                                        onlineUserId + "accepted" + message.getSender()
                                                + "'s group file-msg successfully");
                            } else {
                                printToServerFrame(
                                        onlineUserId + "failed to accept" + message.getSender() + "'s group file-msg!");
                            }
                        }
                    }
                } else if (type.equals(MessageType.CLIENT_EXIT)) {
                    /*
                     * 客户端调用UserClinetService的logout()方法 向服务端发送退出请求
                     * 服务端接受到退出请求，将msg返回给客户端线程，再关闭对应的socket
                     */
                    ServerConnectThread thread = ServerConnectThreadManage.getThread(message.getSender(),
                            message.getGetter());
                    MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                    oos.writeObject(message);
                    if (message.getGetter().equals("在线")) {
                        ServerConnectThreadManage.removeThread(userId);
                        printToServerFrame(userId + " exited successfully.");
                    } else if (message.getGetter().equals("群聊")) {
                        ServerConnectThreadManage.removeThread(userId, "群聊");
                        printToServerFrame(userId + " ended group chat.");
                    } else {
                        ServerConnectThreadManage.removeThread(userId, message.getGetter());
                        printToServerFrame(userId + " ended private chat with " + message.getGetter() + ".");
                    }
                    socket.close();
                    break;
                } else {
                    printToServerFrame("[ERROR] Unknown message type.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void printToServerFrame(String msg) {
        if (msg != null) {
            serverFrame.getShowcase().setText(serverFrame.getShowcase().getText() + msg + "\n");
            System.out.println(msg);
        }
    }
}
