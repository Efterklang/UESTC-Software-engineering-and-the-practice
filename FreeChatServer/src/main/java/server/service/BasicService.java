package server.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import common.Message;
import common.MessageType;
import common.User;
import server.utils.MyObjectInputStream;
import server.utils.MyObjectOutputStream;
import server.view.ServerFrame;

public class BasicService {
    private ServerSocket serverSocket = null;
    private ServerFrame serverFrame = null;
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();

    static {
        // Read the file and initialize validUsers
        try (BufferedReader br = new BufferedReader(new FileReader("path/to/file.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String userId = parts[0];
                User user = new User(/* initialize user object with remaining parts */);
                validUsers.put(userId, user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BasicService(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
        try (ServerSocket serverSocket = new ServerSocket(9999)) {
            this.serverSocket = serverSocket;
            while (true) {
                // Listen for socket and receive User
                Socket socket = serverSocket.accept();
                // 接受客户端传来的User对象
                MyObjectInputStream ois = new MyObjectInputStream(socket.getInputStream());
                User user = (User) ois.readObject();

                // Verify user login
                MyObjectOutputStream oos = new MyObjectOutputStream(socket.getOutputStream());
                Message message = new Message();
                if (user.getRegistMessageType() != null
                        && user.getRegistMessageType().equals(MessageType.REGIST_REQUEST)) {
                    if (true) { //todo
                        
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void printToServerFrame(String msg) {
        if (msg != null) {
            serverFrame.getShowcase().setText(serverFrame.getShowcase().getText() + msg + "\n");
        }
    }
}
