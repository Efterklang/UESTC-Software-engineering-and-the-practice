package server.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

import common.User;
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
    }

}
