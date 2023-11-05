package server.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import common.User;

public class ProcessAccountData {
    String path = "E:/UESTC/Chat/freechat/FreeChatServer/src/main/resources/account.txt"; // "account.txt"
    
    public void readAccountFile(ConcurrentHashMap<String, User> validUsers) {
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                User user = new User(username, password);
                validUsers.put(username, user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAccountFile(String userId, String pwd) {
        File file = new File(path);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file,true))) {
            bw.write(userId + "," + pwd + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // public static void main(String[] args) {
    //     ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();
    //     User user1 = new User("admin", "123456");
    //     User user2 = new User("admin2", "123");
    //     ProcessAccountData processAccountData = new ProcessAccountData();
    //     processAccountData.writeAccountFile("admin", "123456");
    //     processAccountData.writeAccountFile("admin2", "123");

    //     processAccountData.readAccountFile(validUsers);
    //     System.out.println(validUsers.get("admin").getPassword());
    //     System.out.println(validUsers.get("admin2").getPassword());
    // }
}
