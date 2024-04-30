package org.example.netty.heart

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import java.io.IOException


// 心跳机制的服务端处理器
class HeartbeatServerHandlerkt : ChannelInboundHandlerAdapter() {
    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, event: Any) {
        // 如果当前触发的事件是闲置事件
        if (event is IdleStateEvent) {
            // 如果对应的Channel通道触发了读闲置事件
            if (event.state() == IdleState.READER_IDLE) {
                // 表示对应的客户端没有发送心跳包，则关闭对应的网络连接
                // （心跳包也是一种特殊的数据，会触发读事件，有心跳就不会进这步）
                ctx.channel().close()
                println("关闭了未发送心跳包的连接....")
            } else {
                super.userEventTriggered(ctx, event)
            }
        }
    }

    @Throws(Exception::class)
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        // 如果收到的是心跳包，则给客户端做出一个回复
        if ("I am Alive" == msg) {
            ctx.channel().writeAndFlush("I know")
        }
        println("收到客户端消息：$msg")
        super.channelRead(ctx, msg)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // 处理异常
        if (cause is IOException && "远程主机强迫关闭了一个现有的连接。" == cause.message) {
            // 远程主机强制关闭连接异常处理逻辑
            // 在这里添加你的处理逻辑，例如记录日志、关闭连接等
            println(ctx.channel().toString() + ", 远程主机强制关闭了一个现有的连接。")
        } else {
            // 其他异常处理逻辑
            cause.printStackTrace()
            ctx.close() // 关闭连接或其他操作
        }
    }
}
