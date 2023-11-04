package client.service;

import java.util.HashMap;

/**
 * @author gjx
 * @apiNote 进行客户端线程的管理
 * <ul>
 * <li>addThread 将Thread包装后的stateMap放入map，key为userId
 * <li>getThread 根据userId和用户状态获取对应的线程
 * <li>removeThread 从map中删除用户所对应的所有线程
 * </ul>
 */
public class ClientConnectServerThreadManage {
    // map<userId, map<state, thread>>
    private static HashMap<String, HashMap<String, ClientConnectServerThread>> map = new HashMap<>();
    // map<state, thread>
    private static HashMap<String, ClientConnectServerThread> stateMap = new HashMap<>();

    /**
     * @apiNote 将线程放入HashMap集合
     * @param userId 用户id
     * @param state 群聊/私聊(此时state为对方用户名)
     * @param thread 线程对象
     */
    public static void addThread(String userId, String state, ClientConnectServerThread thread) {
        stateMap.put(state, thread);
        map.put(userId, stateMap);
    }

    /**
     * @apiNote 根据userId和用户状态创建一个线程
     * @param userId 用户id
     * @param state 用户状态 在线/离线/群聊/私聊
     * @return ClientConnectThread 线程对象
     */
    public static ClientConnectServerThread getThread(String userId, String state) {
        return map.get(userId).get(state);
    }

    /**
     * @apiNote 从集合中删除用户的所有线程
     * @param userId 用户id
     */
    public static void removeThread(String userId) {
        map.remove(userId);
    }

    public static HashMap<String, HashMap<String, ClientConnectServerThread>> getMap() {
        return map;
    }

    public static HashMap<String, ClientConnectServerThread> getStateMap() {
        return stateMap;
    }
}
