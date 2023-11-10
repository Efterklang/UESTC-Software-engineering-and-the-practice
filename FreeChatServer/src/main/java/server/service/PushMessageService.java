package server.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JOptionPane;

import common.Message;
import common.MessageType;
import server.utils.MyObjectOutputStream;
import server.view.ServerFrame;

/**
 * @apiNote 服务器推送消息服务
 * @author gjx
 */
public class PushMessageService {
    private ServerFrame serverFrame = null;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    public PushMessageService(ServerFrame serverFrame) {
        this.serverFrame = serverFrame;
    }

    public void pushMsg(String msg) {
        // initialize message
        Message message = new Message();
        message.setSender("\n========== FreeChat推送 =========\n");
        message.setContent("\t\t\t" + msg + "\n\n======== End ======\n");
        message.setMessType(MessageType.GROUP_MES);
        message.setSendTime(sdf.format(new Date()));
        printToServerFrame("Server 推送消息:" + msg);
        /*
         * 调用线程管理类 的方法得到 集合
         * 循环所有通信线程 得到socket,推送消息
         */
        HashMap<String, HashMap<String, ServerConnectThread>> map = ServerConnectThreadManage.getMap();
        for (String onlineUserId : map.keySet()) {
            try {
                ServerConnectThread thread = map.get(onlineUserId).get("群聊");
                if (thread != null) {
                    MyObjectOutputStream oos = new MyObjectOutputStream(thread.getSocket().getOutputStream());
                    message.setGetter(onlineUserId);
                    oos.writeObject(message);
                } else {
                    JOptionPane.showMessageDialog(null, "推送给" + onlineUserId + "失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serverFrame.getPushTxt().setText("");
    }

    /**
     * @apiNote 将消息打印到服务器界面
     * @param msg 消息
     */
    public void printToServerFrame(String msg) {
        if (msg != null) {
            serverFrame.getShowcase().setText(serverFrame.getShowcase().getText() + msg + "\n");
        }
    }
}
