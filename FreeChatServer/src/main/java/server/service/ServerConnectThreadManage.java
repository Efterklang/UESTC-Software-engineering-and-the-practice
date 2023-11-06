package server.service;

import java.util.HashMap;
import java.util.Iterator;

/**
 * @author gjx
 * @version 1.
 * @apiNote 服务器管理用户线程类
 *          <ul>
 *          <li>HashMap<String, HashMap<String, ServerConnetThread>>
 *          <li>用户id-HashMap<状态(对方id)-线程>
 *          <li>addThread(String userId, String state, ServerConnetThread
 *          thread)
 *          <li>新建一个线程，加入到map中
 *          <li>getThread(String userId, String state) 根据id返回线程
 *          <li>removeThread(String userId, String state)
 *          当用户关闭与某一个用户的聊天窗口时，删除该线程
 *          </ul>
 */
public class ServerConnectThreadManage {
    private static HashMap<String, HashMap<String, ServerConnectThread>> map = new HashMap<>();

    public static HashMap<String, HashMap<String, ServerConnectThread>> getMap() {
        return map;
    }

    /**
     * @apiNote 新建一个线程，加入到map中
     * @param userId
     * @param state
     * @param thread
     */
    public static void addThread(String userId, String state, ServerConnectThread thread) {
        if (map.get(userId) != null) {
            // 用户存在，得到用户的线程集合，添加一个新的线程
            map.get(userId).put(state, thread);
        } else {
            // 用户不存在，新建一个用户的线程集合，添加一个新的线程
            HashMap<String, ServerConnectThread> stateMaps = new HashMap<>();
            stateMaps.put(state, thread);
            map.put(userId, stateMaps);
        }
    }

    /**
     * @apiNote 根据id返回线程
     * @param userId 用户id
     * @param state  对方id
     * @return ServerConnectThread
     */
    public static ServerConnectThread getThread(String userId, String state) {
        return map.get(userId).get(state);
    }

    /**
     * @apiNote 当用户关闭与某一个用户的聊天窗口时，删除该线程
     * @param userId 用户id
     * @param state  对方id
     */
    public static void removeThread(String userId, String state) {
        map.get(userId).remove(state);
    }

    /**
     * @apiNote 当用户退出时，删除ta的所有线程
     * @param userId 用户id
     */
    public static void removeThread(String userId) {
        map.remove(userId);
    }

    /**
     * @apiNote 在线用户列表 用" "分割，储存所有的UserID
     * @return 在线用户列表
     */
    public static String getOnlineUsers() {
        Iterator<String> iterator = map.keySet().iterator();
        StringBuilder onlineUserList = new StringBuilder();
        while (iterator.hasNext()) {
            // 取出在线用户的id 即为iterator 用" "分割
            onlineUserList.append(iterator.next()).append("\n");
        }
        return onlineUserList.toString();
    }
}
