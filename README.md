# Akka-Demo

akka-imDemo：akka框架结合路由模式实现的聊天室
akka-clusterDemo2：akka框架分片集群+集群发布订阅模式

Netty服务端：ChatServerKt

Netty客户端：ChatClientKt

```
├─akka-clusterDemo2
│  ├─src
│  │  ├─main
│  │  │  ├─java
│  │  │  │  └─org
│  │  │  │      └─example
│  │  │  │          └─netty
│  │  │  │              ├─akka akka相关Actor
│  │  │  │              ├─codec
│  │  │  │              ├─heart
│  │  │  │              ├─message
│  │  │  │              │  └─dao
│  │  │  │              └─msg
│  │  │  └─resources
│  │  └─test
│  │      └─java
└─akka-imDemo
    ├─src
    │  ├─main
    │  │  ├─java
    │  │  │  └─org
    │  │  │      └─example
    │  │  │          ├─demo
    │  │  │          └─netty
    │  │  │              ├─akka akka相关Actor
    │  │  │              ├─codec
    │  │  │              ├─heart
    │  │  │              ├─message
    │  │  │              │  └─dao
    │  │  │              └─msg
    │  │  └─resources

```