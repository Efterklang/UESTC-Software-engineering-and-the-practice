# Masscan SCTP扫描快速参考指南

## 快速导航

如果你想了解masscan如何实现SCTP协议扫描，请参考：
- 详细文档：[masscan-sctp-implementation.md](./masscan-sctp-implementation.md)

## 核心问题快速解答

### 问：SCTP扫描配置在哪里实现？

**答：** 主要在以下源文件中：

| 文件 | 作用 |
|------|------|
| `src/proto-sctp.c` | **核心实现** - SCTP消息构造和响应处理 |
| `src/proto-sctp.h` | 头文件定义 - 结构体和常量 |
| `src/templ-pkt.c` | 数据包模板 - SCTP数据包生成 |
| `src/masscan.c` | 主程序 - 协议选择逻辑 |

### 问：SCTP消息是如何构造的？

**答：** SCTP扫描使用INIT chunk进行端口探测：

```c
// 1. SCTP通用头部（12字节）
struct SCTP_Header {
    uint16_t source_port;       // 源端口
    uint16_t destination_port;  // 目标端口
    uint32_t verification_tag;  // 验证标签（INIT时为0）
    uint32_t checksum;          // CRC32c校验和
};

// 2. INIT Chunk（20字节最小）
struct SCTP_Init_Chunk {
    uint8_t  chunk_type;        // 类型 = 1 (INIT)
    uint8_t  chunk_flags;       // 标志
    uint16_t chunk_length;      // 长度
    uint32_t initiate_tag;      // 初始标签
    uint32_t advertised_rwnd;   // 接收窗口
    uint16_t outbound_streams;  // 出站流数
    uint16_t inbound_streams;   // 入站流数
    uint32_t initial_tsn;       // 初始TSN
};
```

**关键点：**
1. verification_tag在INIT消息中必须为0
2. 使用CRC32c校验和（不是传统CRC32）
3. 端口开放时会收到INIT-ACK响应
4. 端口关闭时会收到ABORT响应

### 问：如何使用masscan扫描SCTP端口？

**答：** 使用`S:`前缀指定SCTP端口：

```bash
# 扫描单个SCTP端口
masscan 192.168.1.0/24 -p S:3868

# 扫描多个SCTP端口
masscan 192.168.1.0/24 -p S:3868,S:2905,S:2944

# 混合扫描TCP和SCTP
masscan 192.168.1.0/24 -p 80,443,S:3868
```

### 问：SCTP常用端口有哪些？

**答：** 常见的SCTP端口包括：

| 端口 | 协议/服务 | 说明 |
|------|----------|------|
| 3868 | DIAMETER | AAA协议 |
| 2905 | M3UA | SS7信令 |
| 2944 | M2UA | SS7信令 |
| 36412 | S1AP | LTE S1接口 |
| 38412 | NGAP | 5G NG接口 |
| 9899 | SCTP Tunnel | SCTP隧道 |
| 11997 | WiMAX | WiMAX协议 |

## 数据包结构图

```
完整SCTP INIT数据包（最小66字节）：

+----------------------+
| Ethernet Header      | 14字节
|  - Dst MAC (6)       |
|  - Src MAC (6)       |
|  - Type (2)          |
+----------------------+
| IP Header            | 20字节
|  - Version/IHL (1)   |
|  - TOS (1)           |
|  - Total Length (2)  |
|  - ID (2)            |
|  - Flags/Offset (2)  |
|  - TTL (1)           |
|  - Protocol=132 (1)  | ← SCTP协议号
|  - Checksum (2)      |
|  - Src IP (4)        |
|  - Dst IP (4)        |
+----------------------+
| SCTP Header          | 12字节
|  - Src Port (2)      |
|  - Dst Port (2)      |
|  - Verify Tag (4)    | ← INIT时为0
|  - Checksum (4)      | ← CRC32c
+----------------------+
| INIT Chunk           | 20字节
|  - Type=1 (1)        |
|  - Flags (1)         |
|  - Length (2)        |
|  - Initiate Tag (4)  |
|  - A_RWND (4)        |
|  - Out Streams (2)   |
|  - In Streams (2)    |
|  - Initial TSN (4)   |
+----------------------+
```

## 关键函数位置

### 1. INIT消息构造
```
文件：src/proto-sctp.c
函数：sctp_create_init_packet()
作用：构造SCTP INIT chunk用于端口探测
```

### 2. CRC32c校验和
```
文件：src/proto-sctp.c
函数：sctp_crc32c()
多项式：0x1EDC6F41
作用：计算SCTP数据包校验和
```

### 3. 响应处理
```
文件：src/proto-sctp.c
函数：handle_sctp_response()
作用：解析INIT-ACK/ABORT响应判断端口状态
```

### 4. 数据包模板
```
文件：src/templ-pkt.c
函数：template_set_target_sctp()
作用：生成完整的SCTP扫描数据包
```

## SCTP vs TCP/UDP对比

| 特性 | TCP | UDP | SCTP |
|------|-----|-----|------|
| 连接 | 面向连接 | 无连接 | 面向连接 |
| 可靠性 | 可靠 | 不可靠 | 可靠 |
| 顺序保证 | 是 | 否 | 可选 |
| 校验和 | 16位校验和 | 16位校验和 | 32位CRC32c |
| 多流 | 否 | 否 | 是 |
| 多宿主 | 否 | 否 | 是 |
| 消息边界 | 否 | 是 | 是 |
| 握手 | 3次 | 无 | 4次 |

## 扫描结果判断

| 响应 | 含义 | 端口状态 |
|------|------|----------|
| INIT-ACK | 收到INIT确认 | **开放** |
| ABORT | 连接被拒绝 | 关闭 |
| ICMP不可达 | 主机/端口不可达 | 过滤/关闭 |
| 无响应 | 超时 | 过滤/关闭 |

## 调试技巧

### 1. 使用Wireshark捕获SCTP流量

```bash
# 启动抓包
tcpdump -i eth0 -w sctp_scan.pcap 'ip proto 132'

# 在Wireshark中过滤
sctp
```

### 2. 查看masscan详细输出

```bash
# 显示发送的数据包
masscan 192.168.1.1 -p S:3868 --packet-trace

# 显示调试信息
masscan 192.168.1.1 -p S:3868 -d
```

### 3. 验证SCTP支持

```bash
# 检查系统SCTP模块
lsmod | grep sctp

# 加载SCTP模块
modprobe sctp
```

## 扩展阅读

- **详细文档**：[masscan-sctp-implementation.md](./masscan-sctp-implementation.md) - 包含完整的源码分析
- **RFC 4960**：SCTP协议标准文档
- **RFC 3309**：SCTP校验和规范
- **Masscan项目**：https://github.com/robertdavidgraham/masscan

## 常见问题

### Q: 为什么我的系统不支持SCTP扫描？

A: 确保：
1. 内核加载了SCTP模块：`modprobe sctp`
2. Masscan版本支持SCTP（v1.0.5+）
3. 网络设备不过滤SCTP流量

### Q: SCTP扫描比TCP慢吗？

A: SCTP使用4次握手，理论上比TCP的3次握手慢一点，但masscan使用无状态扫描，实际速度差异很小。

### Q: 如何确认端口真的使用SCTP？

A: 使用nmap的服务探测：
```bash
nmap -sV --script=sctp-enum-services -p 3868 192.168.1.1
```

### Q: SCTP扫描有什么实际用途？

A: 主要用于：
1. 电信网络设备扫描（DIAMETER, SS7）
2. 4G/5G核心网安全评估
3. IMS（IP多媒体子系统）审计
4. WebRTC和SIP over SCTP检测

---

**提示**：本快速参考基于masscan最新版本。具体实现细节请参考[完整文档](./masscan-sctp-implementation.md)。
