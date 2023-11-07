package server.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import common.User;

/**
 * @apiNote 🛠️使用IO流+ConcurrentHashMap模拟数据库，后期可用JDBC+MySQL代替;
 * <ul>
 * <li> readAccountFile: 读取数据库(account.properties)中的账号数据，写入到validUsers中;
 *      当程序启动时，需要调用该方法(放在static {}中)，将数据库中的数据读取到validUsers中;
 *      当对数据库进行修改时，需要调用该方法，将修改后的数据写入到validUsers中;
 * <li> writeAccountFile: 当进行注册时，将userId＆pwd写入到数据库中(account.properties)
 * </ul>
 * @author gjx
 * @see common.User
 */
public class ProcessAccountData {
    ClassLoader classLoader = ProcessAccountData.class.getClassLoader();
    private URL url = classLoader.getResource("account.properties");
    private final String path = url.getPath();
    
    /**
     * @apiNote 对数据库(account.properties,见target/resource)进行读取，写入到validUsers
     * @param validUsers 要写入账号数据的HashMap
     * @throws IOException
     */
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


    /**
     * @apiNote 当进行注册时，将userId＆pwd写入到数据库中(account.properties)
     * @param userId 用户id
     * @param pwd 用户密码
     * @throws IOException
     */
    public void writeAccountFile(String userId, String pwd) {
        File file = new File(path);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(userId + "," + pwd + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
