package client.server;

import java.net.Socket;

import client.utils.MyObjectInputStream;
import client.view.ChatFrame;
import client.view.OnlineFriendsListFrame;
import common.Message;
import common.MessageType;

/**
 * @apiNote 客户端线程类，进行客户端与服务端的通信
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
            try (MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());) {
                Message message = (Message) ois.readObject();
                if (message.getMessType().equals(MessageType.RETURN_ONLINE_FRIEND)) {
                    //todo
                }
            } catch (Exception e) {
            }
        }
    }

    public void println(String s) {
        if (s != null) {
            // Todo
            System.out.println(s + "\n");
        }
    }

    public void onlineUserPrintln(String s) {
        if (s != null) {
            onlineFriendsListFrame.getShowcase().setText(s + "\n");
            System.out.println(s + "\n");
        }
    }

    /* setter getter */
    public Socket getSocket() {
        return socket;
    }

    public ChatFrame getChatFrame() {
        return chatFrame;
    }

    public void setChatFrame(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
    }
}
