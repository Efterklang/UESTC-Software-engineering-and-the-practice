package client.view;

import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

import common.User;

public class clientService {
    private User user = new User();
    private Socket socket;

    public boolean checkUser(String userId, String password) {
        boolean loop = false;
        try {
            user.setUserId(userId);
            user.setPassword(password);
            //TODO
        } catch (Exception e) {
        } 
    }

}
