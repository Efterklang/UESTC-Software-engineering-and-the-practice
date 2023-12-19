## [FreeChat ](https://github.com/Efterklang/FreeChat)

### <font color = "426ab3">Intro </font>

> **FreeChat Project**


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
* **多线程：使用ConcurrentHashMap管理用户的线程集合，Thread集合负责维护Socket**
* **GUI：实现图形化交互界面，使用java awt＆swing**
* **git＆maven: 项目管理**
* **GitHub Actions: 使用github workflows实现CI(持续继承)**
* **使用I/O(写入到account.properties文件中)+HashMap模拟数据库**
* **Launch4j：将jar包生成为.exe文件**

### 代码规范-Annotation

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
