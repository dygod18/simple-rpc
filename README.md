# simple-rpc
### 简介
基于netty 实现的demo版本的RPC 框架，框架实现了服务暴露、客户端直连调用、协议报文编解码等功能；注册中心、负载均衡等功能还未实现；目前可以实现简单的直连环境下的调用。

### HOW TO USE
- 第一步：clone代码到本地
- 启动 com.wumu.rpc.simplerpc.boot.ServerBoot
> 服务提供方，会默认提供一个HelloService。启动成功后可以看到server启动成功的log
- 运营com.wumu.rpc.simplerpc.boot.ClientBoot
> 服务调用方，内部会轮询一千次调用HelloService的sayHello方法

### 服务调用流程图
![Alt text](https://github.com/dygod18/simple-rpc/blob/master/doc/simple-rpc%20客户端调用流程图.png)
