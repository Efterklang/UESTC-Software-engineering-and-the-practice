package client.server;

import java.util.HashMap;

/**
 * @author gjx
 * @apiNote 进行客户端线程的管理
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
     * @apiNote 根据userId和用户在线状态创建一个线程
     * @param userId 用户id
     * @param state 用户状态 在线/离线/群聊/私聊
     * @return ClientConnectThread 线程对象
     */
    public static ClientConnectServerThread getThread(String userId, String state) {
        return map.get(userId).get(state);
    }

    /**
     * @apiNote 从集合中删除线程对象
     * @param userId
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
