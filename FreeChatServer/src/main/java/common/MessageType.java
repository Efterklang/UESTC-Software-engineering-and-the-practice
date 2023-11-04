package common;

/**
 * @apiNote 消息类型常量类
 * <ul>
 * <li>LOGIN_SUCCEED 登录成功
 * <li>LOGIN_FAIL 登录失败
 * <li>COMMON_MES 普通消息包(私聊)
 * <li>GET_ONLINE_FRIEND 客户端 请求返回在线消息列表
 * <li>RETURN_ONLINE_FRIEND 服务端 返回在线用户列表
 * <li>CLIENT_EXIT 客户端请求退出
 * <li>GROUP_MES 客户端群发消息
 * <li>DIRECT_FILE_MES 客户端 发送文件
 * <li>REGIST_REQUEST 客户端 请求注册账户
 * <li>REGIST_SUCCEED 注册成功
 * <li>REGIST_FAIL 注册失败
 * <li>GROUP_FILE_MES 客户端 群发文件
 * </ul>
 */
public interface MessageType {
    String LOGIN_SUCCEED = "1"; // 登录成功
    String LOGIN_FAIL = "2"; // 登录失败
    String COMMON_MES = "3"; // 普通消息包(私聊)
    String GET_ONLINE_FRIEND = "4"; // 客户端 请求返回在线消息列表
    String RETURN_ONLINE_FRIEND = "5"; // 服务端 返回在线用户列表
    String CLIENT_EXIT = "6"; // 客户端请求退出
    String GROUP_MES = "7"; // 客户端群发消息
    String DIRECT_FILE_MES = "8"; // 客户端 发送文件
    String REGIST_REQUEST = "9"; // 客户端 请求注册账户
    String REGIST_SUCCEED = "10"; // 注册成功
    String REGIST_FAIL = "11"; // 注册失败
    String GROUP_FILE_MES = "12"; // 客户端 群发文件
}
