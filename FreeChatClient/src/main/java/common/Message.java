package common;

import java.io.Serializable;

/**
 * @author gjx
 * @apiNote 客户端和服务端通信时的消息对象
 * @version 2.0(新增文件传输)
 * @see common.MessageType
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender; // 发送者
    private String getter; // 接收者 
	private String content;// 发送内容 
	private String sendTime;// 发送时间
    private String messType;// 消息类型,在MessageType接口中定义
    private String state;
    private int fileLength;
    private byte[] fileBytes;
    private String fileName;
    private String srcPath;
    private String destPath;
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
    public int getFileLength() {
        return fileLength;
    }
    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }
    public byte[] getFileBytes() {
        return fileBytes;
    }
    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getSrcPath() {
        return srcPath;
    }
    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }
    public String getDestPath() {
        return destPath;
    }
    public void setDestPath(String destPath) {
        this.destPath = destPath;
    }
    
}