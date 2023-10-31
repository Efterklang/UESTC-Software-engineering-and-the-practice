package client.server;

import java.util.HashMap;

/**
 * 该类用于管理客户端连接到服务端的线程
 * 
 * @author gjx
 */
public class ClientConnectThraedManage {
    //map<userId, map<state, thread>>
    private static HashMap<String, HashMap<String, ClientConnectThread>> map = new HashMap<>();
    //map<state, thread>
    private static HashMap<String, ClientConnectThread> stateMap = new HashMap<>();
    /**
     * 将线程放入HashMap集合
     * @param userId
     * @param state
     * @param thread
     */
    public static void addThread(String userId, String state, ClientConnectThread thread) {
        stateMap.put(state, thread);
        map.put(userId, stateMap);
    }

    /**
     * 根据userId和用户在线状态返回线程
     * @param userId
     * @param state
     * @return
     */
    public static ClientConnectThread getThread(String userId, String state) {
        return map.get(userId).get(state);
    }
    /**
     * 从集合中删除线程对象
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
