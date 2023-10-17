package common;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sender;
    private String getter; // 接收者 
	private String content;// 发送内容 
	private String sendTime;// 发送时间
    private String messType;// 消息类型,在MessageType接口中定义

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

    

}