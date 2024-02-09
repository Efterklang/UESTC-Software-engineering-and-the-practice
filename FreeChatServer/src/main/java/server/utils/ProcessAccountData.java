package server.utils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import common.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @apiNote 🛠️使用IO流+MySQL代替;
 *          <ul>
 *          <li>readAccountFile: 读取数据库中的账号数据，写入到validUsers中;
 *          当程序启动时，需要调用该方法(放在static {}中)，将数据库中的数据读取到validUsers中;
 *          当对数据库进行修改时，需要调用该方法，将修改后的数据重新写入到validUsers中;
 *          <li>writeAccountFile: 当进行注册时，将userId＆pwd写入到数据库中
 *          </ul>
 * @author gjx
 * @see common.User
 */
public class ProcessAccountData {

    ClassLoader classLoader = ProcessAccountData.class.getClassLoader();
    final String sqlURL =
            "jdbc:mysql://localhost:3306/free_chat?serverTimezone=GMT&useSSL=false&allowPublicKeyRetrieval=true";
    final String user = "root";
    final String password = "gjxMySQLPWD";

    /**
     * @apiNote 对数据库(account.properties,见target/resource)进行读取，写入到validUsers
     * @param validUsers 要写入账号数据的HashMap
     * @throws IOException
     */
    public void readAccountFile(ConcurrentHashMap<String, User> validUsers) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try (Connection conn = DriverManager.getConnection(sqlURL, user, password);
                Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("select * from accounts");
            while (rs.next()) {
                User user = new User(rs.getString("account"), rs.getString("pwd"));
                validUsers.put(rs.getString("account"), user);
            }
        } catch (SQLException e) {
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
        String sql = "INSERT INTO accounts (account, pwd) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(sqlURL, user, password);) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, pwd);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}