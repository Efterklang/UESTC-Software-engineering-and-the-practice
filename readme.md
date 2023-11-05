## FreeChat项目

### `<font color = "426ab3">`Intro `</font>`

> **FreeChat Project**
> 当不安汹涌而来，对世界与生命的思索，对未来与知识的探讨，变得稀缺而困难起来。但即使身处浮躁的浪潮，我们也禁不住畅想，同处一个时代下的异国的同辈，他们此时在想些什么？他们又如何看待这个世界和彼此年轻的生活呢？如果你也和我们一样，欢迎来到这场，举世共舞的冒险。
>
> When unease surges in, contemplation of the world and life, exploration of the future and knowledge, become scarce and difficult. But even in the midst of impetuous waves, we cannot help but imagine what our foreign peers in the same era are thinking at this moment? How do they view this world and each other's young lives? If you are like us, welcome to this adventure of dancing with the world.
>
> 欢迎来到Freechat 项目！！！
> Welcome to Freechat ！！

---

### ✨预期实现功能

**客户端**

* **客户端 能够群聊，私聊(双方用户都在线时能互相收发消息，后续如果有能力添加离线收发的功能)；**
* **能够私聊或群聊收发文件并保存**
* **用户可以拉取在线用户的列表**
* **客户端无异常启动＆退出**

**服务端**

* **实现注册功能，验证用户登录并更新信息**
* **向群聊窗口推送信息**

### 🛠️使用技术

* **TCP网络编程**
* **多线程：使用ThreadPool管理Socket的线程集合**
* **GUI：实现图形化交互界面**
* **git＆maven: 项目管理**
* **MySql+JDBC记录用户信息；或者使用IO流+HashMap模拟数据库(根据学习情况决定)**

### 代码规范

* 类/接口命名 大驼峰

  ```java
  /**
   * @apiNote xxx
   * @author xxx
   */
  public class ClientFileService {
      //todo
  }
  ```
* 成员变量命名 小驼峰

  ```java
  /**
   * @apiNote 发送文件给所有人
   * @param senderId  发送者id
   * @param getterId  接收者id
   * @param filePath  文件路径
   * @param fileName  文件名
   * @param chatFrame 聊天框
   */
  public void sendDirectFile(String senderId, String getterId, String filePath, String fileName,ChatFrame chatFrame)
  ```

**ClientConnectServerThread对群发文件缺少处理.**
