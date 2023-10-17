package common;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    /*
     * Version 1.0
     * 功能：表示 客户端和服务端 通信时的消息对象
     * 包括5个字段：发送者、接收者、发送内容、发送时间、消息类型
    */
    private String sender;
    private String getter; // 接收者 
	private String content;// 发送内容 
	private String sendTime;// 发送时间
    private String messType;// 消息类型,在MessageType接口中定义
    /*
     * Version 2.0
     * 功能：增加用户状态字段
     */
    private String state;
    
    // getter and setter
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getGetter() {
        return getter;
    }
    public void setGetter(String getter) {
        this.getter = getter;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSendTime() {
        return sendTime;
    }
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
    public String getMessType() {
        return messType;
    }
    public void setMessType(String messType) {
        this.messType = messType;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
}