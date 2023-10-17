package common;

import java.io.Serializable;

public class User implements Serializable {
    public static final long serialVersionUID = 1L;

    private String userId; // 账号
    private String password; // 密码
    private String registMessageType; // 是否请求注册

    private String state = null; // 设置发送状态
    
    // constructor
    public User() {
    }
    
    /**
     * 创建一个新的用户对象。
     * 
     * @param userId   用户 ID。
     * @param password 用户密码。
     */
    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    // getter and setter
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistMessageType() {
        return registMessageType;
    }

    public void setRegistMessageType(String registMessageType) {
        this.registMessageType = registMessageType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
