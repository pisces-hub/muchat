# MuChat

<br/>

> 1. **前端项目地址**：[muchat-ui](https://gitee.com/pisces-hub/muchat-ui)
> 2. **快速体验项目**：[在线访问地址](http://43.138.164.74)


## 前言

`Muchat`100w级即时通讯应用


## 项目介绍

`Muchat`是用JAVA语言开发的轻量、高性能、单机支持几十万至百万在线用户IM，主要目标降低即时通讯门槛，快速打造低成本接入在线IM系统，通过极简洁的消息格式就可以实现多端不同协议间的消息发送如内置(Http、Websocket、Tcp自定义IM协议)


### 组织结构

``` lua
muchat
├── im-common -- 工具类及通用代码
├── im-connector -- 长连接服务
├── im-sdk -- 发送消息功能，封装公用模块
├── im-server -- 业务服务
└── im-admin -- 后台管理服务
```

### 技术选型

#### 后端技术

| 技术                 | 说明                | 官网                                           |
| -------------------- | ------------------- | ---------------------------------------------- |
| SpringBoot           | Web应用开发框架      | https://spring.io/projects/spring-boot         |
| MyBatis              | ORM框架             | http://www.mybatis.org/mybatis-3/zh/index.html |
| Redis                | 内存数据存储         | https://redis.io/                              |
| Nginx                | 静态资源服务器      | https://www.nginx.com/                         |
| MinIO                | 对象存储            | https://github.com/minio/minio                 |


#### 架构图

![系统架构图](docs/images/architecture.jpeg)

### 开发环境

| 工具        | 版本号   | 下载                                             |
|-----------|-------|------------------------------------------------|
| JDK       | 11    |   https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html                                          |
| Mysql     | 8     | https://www.mysql.com/                         |
| Redis     | 7.0   | https://redis.io/download                      |
| Nginx     | 1.22  | http://nginx.org/en/download.html              |
| Zookeeper | 3.8.0 | https://www.apache.org/dyn/closer.lua/zookeeper/zookeeper-3.8.0/apache-zookeeper-3.8.0-bin.tar.gz |


### 搭建步骤

> Windows环境部署

- 克隆`muchat`项目，并导入到IDEA中完成编译
- 启动长连接服务im-connector
- 启动业务服务im-server




### 项目运行截图

![聊天列表](docs/images/demo1.jpg)
![聊天列表](docs/images/demo3.jpg)

## Contributing

- For bug reports, please use [Issues](https://gitee.com/pisces-hub/muchat/issues)
- For code contribution, please use [Pull Request](https://gitee.com/pisces-hub/muchat/pulls)


## 联系方式


![](./docs/images/wx-public.png)

有任何问题，欢迎给我留言哦




## 点下star吧
喜欢的朋友麻烦点个star，鼓励一下作者吧！



## 贡献者列表
<p>
<a href="https://gitee.com/imalasong" target="_blank">
<img src="docs/images/assets/developer/imalasong.png" width="12%">
</a>
<a href="https://gitee.com/xiaochangbai" target="_blank">
<img src="docs/images/assets/developer/xiaochangbai.png" width="12%">
</a>
<a href="https://gitee.com/ilovea" target="_blank">
<img src="docs/images/assets/developer/ilovea.png" width="12%">
</a>
<a href="https://gitee.com/uimoa" target="_blank">
<img src="docs/images/assets/developer/uimoa.png" width="12%">
</a>
<a href="https://gitee.com/tomlia" target="_blank">
<img src="docs/images/assets/developer/tomlia.png" width="12%">
</a>
</p>


## 许可证

[Apache License 2.0](https://github.com/pisces-hub/muchat/blob/develop/LICENSE)

Copyright (c) 2020-2023 muchat
