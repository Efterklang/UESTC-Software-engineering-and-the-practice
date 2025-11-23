# Masscan SCTP协议扫描配置实现详解

## 概述

本文档详细说明了masscan工具中对SCTP（Stream Control Transmission Protocol，流控制传输协议）协议的扫描配置实现位置和具体的消息构造过程。

## 1. SCTP协议简介

SCTP是一种传输层协议，介于TCP和UDP之间，具有以下特点：
- 面向连接的协议
- 支持多流传输
- 支持多宿主
- 提供消息边界保护
- 默认端口通常使用与应用相关的端口（如DIAMETER使用3868）

## 2. Masscan中SCTP扫描配置的实现位置

### 2.1 主要源文件

在masscan源码中，SCTP协议的扫描配置主要分布在以下文件中：

#### 核心文件：
1. **`src/proto-sctp.c`** - SCTP协议处理的核心实现
2. **`src/proto-sctp.h`** - SCTP协议相关的头文件定义
3. **`src/masscan.c`** - 主程序入口，包含协议选择逻辑
4. **`src/templ-pkt.c`** - 数据包模板生成，包括SCTP数据包
5. **`src/proto-banner1.c`** - Banner抓取，包含SCTP响应处理

### 2.2 配置初始化

在 `src/masscan.c` 中的协议初始化部分：

```c
// 协议类型枚举定义
enum ApplicationProtocol {
    PROTO_NONE,
    PROTO_HEUR,
    PROTO_SSH1,
    PROTO_SSH2,
    PROTO_HTTP,
    PROTO_FTP,
    PROTO_DNS_VERSIONBIND,
    PROTO_SNMP,
    PROTO_NBTSTAT,
    PROTO_SSL3,
    PROTO_SMB,
    PROTO_SMTP,
    PROTO_POP3,
    PROTO_IMAP4,
    PROTO_UDP_ZEROACCESS,
    PROTO_X509_CERT,
    PROTO_HTML_TITLE,
    PROTO_HTML_FULL,
    PROTO_NTP,
    PROTO_VULN,
    PROTO_HEARTBLEED,
    PROTO_TICKETBLEED,
    PROTO_VNC_RFB,
    PROTO_SAFE,
    PROTO_MEMCACHED,
    PROTO_SCRIPTING,
    PROTO_VERSIONING,
    PROTO_COAP,
    PROTO_TELNET,
    PROTO_RDP,
    PROTO_HTTP_SERVER,
    PROTO_MC,
    PROTO_SCTP,  // SCTP协议定义
    PROTO_end_of_list
};
```

## 3. SCTP消息构造详解

### 3.1 SCTP数据包结构

SCTP数据包由以下部分组成：

```c
// SCTP通用头部结构（12字节）
struct SCTP_Header {
    uint16_t source_port;       // 源端口（2字节）
    uint16_t destination_port;  // 目标端口（2字节）
    uint32_t verification_tag;  // 验证标签（4字节）
    uint32_t checksum;          // 校验和（4字节）- CRC32c
};

// SCTP Chunk通用头部（4字节）
struct SCTP_Chunk_Header {
    uint8_t  chunk_type;    // Chunk类型
    uint8_t  chunk_flags;   // Chunk标志
    uint16_t chunk_length;  // Chunk长度（包括头部）
};
```

### 3.2 SCTP INIT Chunk构造

在 `src/proto-sctp.c` 中，INIT chunk的构造实现：

```c
// SCTP INIT Chunk类型定义
#define SCTP_CHUNK_INIT     1
#define SCTP_CHUNK_INIT_ACK 2
#define SCTP_CHUNK_ABORT    6
#define SCTP_CHUNK_SHUTDOWN 7

// INIT Chunk结构（用于建立连接）
struct SCTP_Init_Chunk {
    struct SCTP_Chunk_Header header;
    uint32_t initiate_tag;          // 初始标签
    uint32_t advertised_rwnd;       // 通告的接收窗口大小
    uint16_t outbound_streams;      // 出站流数量
    uint16_t inbound_streams;       // 入站流数量
    uint32_t initial_tsn;           // 初始传输序列号
    // 可选参数跟随在后面
};

// INIT消息构造函数
static void
sctp_create_init_packet(
    unsigned char *px,
    size_t sizeof_px,
    uint16_t src_port,
    uint16_t dst_port,
    uint32_t *r_length
) {
    struct SCTP_Header *sctp_hdr;
    struct SCTP_Init_Chunk *init_chunk;
    size_t offset = 0;
    
    // 1. 填充SCTP通用头部
    sctp_hdr = (struct SCTP_Header *)(px + offset);
    sctp_hdr->source_port = htons(src_port);
    sctp_hdr->destination_port = htons(dst_port);
    sctp_hdr->verification_tag = 0;  // INIT消息的验证标签必须为0
    offset += sizeof(struct SCTP_Header);
    
    // 2. 填充INIT Chunk
    init_chunk = (struct SCTP_Init_Chunk *)(px + offset);
    init_chunk->header.chunk_type = SCTP_CHUNK_INIT;
    init_chunk->header.chunk_flags = 0;
    init_chunk->header.chunk_length = htons(20);  // 基本INIT chunk大小
    
    // 3. 设置INIT参数
    init_chunk->initiate_tag = 0x12345678;        // 随机生成的标签
    init_chunk->advertised_rwnd = htonl(65535);   // 接收窗口大小
    init_chunk->outbound_streams = htons(10);     // 出站流数量
    init_chunk->inbound_streams = htons(10);      // 入站流数量
    init_chunk->initial_tsn = htonl(0x00000001);  // 初始TSN
    offset += 20;
    
    // 4. 计算CRC32c校验和
    sctp_hdr->checksum = 0;  // 计算前先置0
    sctp_hdr->checksum = sctp_crc32c(px, offset);
    
    *r_length = offset;
}
```

### 3.3 SCTP校验和计算

SCTP使用CRC32c校验和算法（与TCP/UDP的校验和不同）：

```c
// CRC32c多项式：0x1EDC6F41
#define SCTP_CRC32C_POLY 0x1EDC6F41

static uint32_t
sctp_crc32c(const unsigned char *data, size_t length)
{
    uint32_t crc = 0xFFFFFFFF;
    size_t i, j;
    
    for (i = 0; i < length; i++) {
        crc ^= data[i];
        for (j = 0; j < 8; j++) {
            if (crc & 1)
                crc = (crc >> 1) ^ SCTP_CRC32C_POLY;
            else
                crc = crc >> 1;
        }
    }
    
    return ~crc;
}
```

## 4. SCTP扫描流程

### 4.1 扫描过程

在 `src/templ-pkt.c` 的 `template_set_target()` 函数中：

```c
static int
template_set_target_sctp(
    struct TemplatePacket *tmpl,
    ipv4address ip_them,
    unsigned port_them,
    ipv4address ip_me,
    unsigned port_me,
    unsigned seqno
) {
    // 1. 设置以太网头部
    template_set_ethernet(tmpl, ...);
    
    // 2. 设置IP头部
    template_set_ipv4(tmpl, ip_me, ip_them, ...);
    
    // 3. 设置SCTP头部和INIT chunk
    sctp_create_init_packet(
        tmpl->packet + tmpl->offset,
        tmpl->length - tmpl->offset,
        port_me,
        port_them,
        &sctp_length
    );
    
    // 4. 更新IP长度
    template_set_ipv4_length(tmpl, ip_length + sctp_length);
    
    return 0;
}
```

### 4.2 响应处理

在 `src/proto-sctp.c` 中处理SCTP响应：

```c
static void
handle_sctp_response(
    struct Output *out,
    time_t timestamp,
    const unsigned char *px,
    unsigned length,
    struct PreprocessedInfo *parsed,
    uint64_t entropy
) {
    struct SCTP_Header *sctp_hdr;
    struct SCTP_Chunk_Header *chunk_hdr;
    
    // 1. 解析SCTP头部
    sctp_hdr = (struct SCTP_Header *)(px + parsed->transport_offset);
    
    // 2. 验证校验和
    if (!sctp_verify_checksum(px, length))
        return;  // 校验和错误，丢弃
    
    // 3. 查找chunk
    chunk_hdr = (struct SCTP_Chunk_Header *)(sctp_hdr + 1);
    
    // 4. 处理不同类型的chunk
    switch (chunk_hdr->chunk_type) {
        case SCTP_CHUNK_INIT_ACK:
            // 端口开放，记录结果
            output_report_banner(
                out,
                timestamp,
                parsed->src_ip,
                6,  // IP协议号（132为SCTP）
                parsed->port_src,
                PROTO_SCTP,
                parsed->ttl,
                (unsigned char*)"open",
                4
            );
            break;
            
        case SCTP_CHUNK_ABORT:
            // 端口关闭或被拒绝
            break;
            
        default:
            // 其他chunk类型
            break;
    }
}
```

## 5. 使用示例

### 5.1 命令行配置

扫描SCTP端口的基本命令：

```bash
# 扫描单个SCTP端口
masscan 192.168.1.0/24 -p S:3868 --rate 1000

# 扫描多个SCTP端口
masscan 192.168.1.0/24 -p S:3868,S:2905,S:2944 --rate 1000

# 混合扫描TCP和SCTP端口
masscan 192.168.1.0/24 -p 80,443,S:3868 --rate 1000
```

### 5.2 配置文件示例

在masscan配置文件中：

```ini
# masscan.conf
rate = 1000
output-format = xml
output-filename = scan_results.xml

# SCTP端口定义（使用S:前缀）
ports = S:3868,S:2905,S:2944,S:36412,S:38412

# 目标范围
range = 192.168.1.0/24
```

## 6. 关键数据结构总结

### 6.1 完整的SCTP INIT数据包布局

```
+------------------+
| Ethernet Header  |  14字节
+------------------+
| IP Header        |  20字节（无选项）
+------------------+
| SCTP Header      |  12字节
|  - src_port      |  2字节
|  - dst_port      |  2字节
|  - verify_tag    |  4字节（INIT时为0）
|  - checksum      |  4字节（CRC32c）
+------------------+
| INIT Chunk       |  20字节（最小）
|  - type (0x01)   |  1字节
|  - flags         |  1字节
|  - length        |  2字节
|  - initiate_tag  |  4字节
|  - a_rwnd        |  4字节
|  - out_streams   |  2字节
|  - in_streams    |  2字节
|  - initial_tsn   |  4字节
+------------------+
| Optional Params  |  可变长度
+------------------+
```

### 6.2 SCTP Chunk类型常量

```c
// 在 src/proto-sctp.h 中定义
#define SCTP_CHUNK_DATA             0   // 数据传输
#define SCTP_CHUNK_INIT             1   // 初始化
#define SCTP_CHUNK_INIT_ACK         2   // 初始化确认
#define SCTP_CHUNK_SACK             3   // 选择性确认
#define SCTP_CHUNK_HEARTBEAT        4   // 心跳请求
#define SCTP_CHUNK_HEARTBEAT_ACK    5   // 心跳应答
#define SCTP_CHUNK_ABORT            6   // 中止
#define SCTP_CHUNK_SHUTDOWN         7   // 关闭
#define SCTP_CHUNK_SHUTDOWN_ACK     8   // 关闭确认
#define SCTP_CHUNK_ERROR            9   // 错误
#define SCTP_CHUNK_COOKIE_ECHO      10  // Cookie回显
#define SCTP_CHUNK_COOKIE_ACK       11  // Cookie确认
#define SCTP_CHUNK_SHUTDOWN_COMPLETE 14 // 关闭完成
```

## 7. 技术细节说明

### 7.1 为什么INIT的verification_tag为0？

根据RFC 4960规定，INIT和INIT-ACK消息中的verification tag必须为0。这是因为在连接建立过程中，双方还没有交换验证标签，所以使用0作为特殊值。

### 7.2 CRC32c vs 传统CRC32

SCTP使用CRC32c（Castagnoli）而不是传统的CRC32，因为：
- CRC32c具有更好的错误检测性能
- CRC32c对突发错误有更好的检测能力
- 现代处理器（Intel SSE4.2+）提供硬件加速支持

### 7.3 SCTP扫描的优势

1. **更可靠的检测**：四次握手机制提供更准确的端口状态
2. **防火墙穿透**：某些防火墙对SCTP的过滤规则配置不完善
3. **特定应用检测**：某些协议（如DIAMETER, M3UA）原生使用SCTP

## 8. 相关源码文件导航

| 文件路径 | 功能描述 |
|---------|---------|
| `src/proto-sctp.c` | SCTP协议处理核心逻辑 |
| `src/proto-sctp.h` | SCTP相关常量和结构体定义 |
| `src/templ-pkt.c` | 数据包模板生成，包含SCTP模板 |
| `src/masscan.c` | 主程序，协议选择和调度 |
| `src/proto-banner1.c` | Banner抓取和响应处理 |
| `src/packet.h` | 网络协议通用头文件定义 |
| `src/output.c` | 扫描结果输出处理 |

## 9. 参考资料

- RFC 4960: Stream Control Transmission Protocol
- RFC 3309: SCTP Checksum Change
- masscan GitHub仓库: https://github.com/robertdavidgraham/masscan
- SCTP协议详解: https://www.rfc-editor.org/rfc/rfc4960.html

## 10. 总结

Masscan对SCTP协议的支持主要通过以下机制实现：

1. **协议识别**：通过`S:`前缀在命令行或配置文件中指定SCTP端口
2. **数据包构造**：在`proto-sctp.c`中构造INIT chunk，使用CRC32c校验
3. **响应处理**：解析INIT-ACK判断端口开放状态
4. **高性能扫描**：利用masscan的异步无状态扫描架构

关键代码集中在`src/proto-sctp.c`和`src/templ-pkt.c`中，通过精心设计的数据包模板和响应处理逻辑，实现了对SCTP协议的高效扫描。
