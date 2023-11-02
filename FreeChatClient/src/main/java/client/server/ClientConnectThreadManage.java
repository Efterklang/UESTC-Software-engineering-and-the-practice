package client.server;

import java.util.HashMap;

/**
 * @author gjx
 * @apiNote 客户端线程管理类
 */
public class ClientConnectThreadManage {
    // map<userId, map<state, thread>>
    private static HashMap<String, HashMap<String, ClientConnectThread>> map = new HashMap<>();
    // map<state, thread>
    private static HashMap<String, ClientConnectThread> stateMap = new HashMap<>();

    /**
     * @apiNote 将线程放入HashMap集合
     * @param userId 用户id
     * @param state 用户状态 在线/离线/群聊/私聊
     * @param thread 线程对象
     */
    public static void addThread(String userId, String state, ClientConnectThread thread) {
        stateMap.put(state, thread);
        map.put(userId, stateMap);
    }

    /**
     * @apiNote 根据userId和用户在线状态创建一个线程
     * @param userId 用户id
     * @param state 用户状态 在线/离线/群聊/私聊
     * @return ClientConnectThread 线程对象
     */
    public static ClientConnectThread getThread(String userId, String state) {
        return map.get(userId).get(state);
    }

    /**
     * @apiNote 从集合中删除线程对象
     * @param userId
     */
    public static void removeThread(String userId) {
        map.remove(userId);
    }

    public static HashMap<String, HashMap<String, ClientConnectThread>> getMap() {
        return map;
    }

    public static HashMap<String, ClientConnectThread> getStateMap() {
        return stateMap;
    }
}
