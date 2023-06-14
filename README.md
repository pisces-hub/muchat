

## 介绍

`MuChat`，中文：沐聊。
一款基于Netty+MQ的高性能IM解决方案，天然的支持水平扩展，代码全部免费开源


## 体验地址
http://43.138.164.74/
账号：admin/123456


## 系统架构
![](docs/images/高性能IM架构.jpg)


## 核心技术

- SpringBoot2.6: 项目基础环境构建与集成其他框架。
- SpringMVC：HTTP请求处理。
- Netty：与客户端建立长连接。
- Zookeeper: 服务注册与发现。
- Redis Queue: 缓存、消息通道。


## TODO LIST


* [x] [私聊](#私聊)
* [x] 根据实际情况灵活的水平扩容、缩容
* [x] [群聊](#群聊)
* [x] emoji表情
* [x] 语音消息
* [x] 支持撤回和删除消息
* [x] 支持视频聊天(基于webrtc实现,需要ssl证书)
* [ ] 使用 `Google Protocol Buffer` 高效编解码
* [ ] 客户端自动重连
* [ ] 采用Hazelcast重构




**支持集群部署。**



## 流程图


- 客户端向 `muchat-server` 发起登录。
- 登录成功从 `Zookeeper` 中选择可用 `muchat-server` 返回给客户端，并保存登录、路由信息到 `Redis`。
- 客户端向 `muchat-server` 发起长连接，成功后保持心跳。
- 客户端下线时通过 `route` 清除状态信息。


## 快速启动

首先需要安装 `Zookeeper、Redis、MySql` 并保证网络通畅

## 联系方式



QQ: 704566072

![](./docs/images/vx1.jpg)



有任何问题，欢迎给我留言哦





## 点下star吧
喜欢的朋友麻烦点个star，鼓励一下作者吧！


## 备注

项目参考 https://gitee.com/bluexsx/box-im