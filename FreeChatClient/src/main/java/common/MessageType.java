package common;

/**
 * @apiNote 消息类型常量类
 * @MESSAGE_LOGIN_SUCCEED 登录成功
 * @MESSAGE_LOGIN_FAIL  登录失败
 * @MESSAGE_COMMON_MES  普通消息包(私聊)
 * @MESSAGE_GET_ONLINE_FRIEND  客户端 请求返回在线消息列表
 * @MESSAGE_RETURN_ONLINE_FRIEND  服务端 返回在线用户列表
 * @MESSAGE_CLIENT_EXIT  客户端请求退出
 * @MESSAGE_GROUP_MES  客户端群发消息
 * @MESSAGE_File_MES  客户端 发送文件
 * @MESSAGE_REGIST_REQUEST  客户端 请求注册账户
 * @MESSAGE_REGIST_SUCCEED  注册成功
 * @MESSAGE_REGIST_FAIL  注册失败
 * @MESSAGE_File_MES_TOALL 客户端 群发文件
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1"; // 登录成功
    String MESSAGE_LOGIN_FAIL = "2"; // 登录失败
    String MESSAGE_COMMON_MES = "3"; // 普通消息包(私聊)
    String MESSAGE_GET_ONLINE_FRIEND = "4"; // 客户端 请求返回在线消息列表
    String MESSAGE_RETURN_ONLINE_FRIEND = "5"; // 服务端 返回在线用户列表
    String MESSAGE_CLIENT_EXIT = "6"; // 客户端请求退出
    String MESSAGE_GROUP_MES = "7"; // 客户端群发消息
    String MESSAGE_File_MES = "8"; // 客户端 发送文件
    String MESSAGE_REGIST_REQUEST = "9"; // 客户端 请求注册账户
    String MESSAGE_REGIST_SUCCEED = "10"; // 注册成功
    String MESSAGE_REGIST_FAIL = "11"; // 注册失败
    String MESSAGE_File_MES_TOALL = "12"; // 客户端 群发文件
}
