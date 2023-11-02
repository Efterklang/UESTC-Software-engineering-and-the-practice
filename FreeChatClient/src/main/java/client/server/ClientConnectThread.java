package client.server;

import java.net.Socket;

import client.utils.MyObjectInputStream;
import client.view.ChatFrame;
import client.view.OnlineFriendsListFrame;
import common.Message;
import common.MessageType;

public class ClientConnectThread extends Thread {
    private Socket socket;
    private ChatFrame chatFrame;
    private OnlineFriendsListFrame onlineFriendsListFrame;

    public ClientConnectThread(Socket socket, ChatFrame chatFrame) {
        super();
        this.socket = socket;
        this.chatFrame = chatFrame;
    }

    public ClientConnectThread(Socket socket, OnlineFriendsListFrame onlineFriendsListFrame) {
        super();
        this.socket = socket;
        this.onlineFriendsListFrame = onlineFriendsListFrame;
    }

    @Override
    public void run() {
        while (true) {
            try (MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());) {
                Message message = (Message) ois.readObject();
                if (message.getMessType().equals(MessageType.MESSAGE_RETURN_ONLINE_FRIEND)) {

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
