package org.example.netty.heart

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.util.CharsetUtil
import java.io.IOException


// 心跳机制的客户端处理器
class HeartbeatClientHandlerkt : ChannelInboundHandlerAdapter() {
    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext, event: Any) {
        // 如果当前触发的事件是闲置事件
        if (event is IdleStateEvent) {
            // 如果当前通道触发了写闲置事件
            if (event.state() == IdleState.WRITER_IDLE) {
                // 表示当前客户端有一段时间未向服务端发送数据了，
                // 为了防止服务端关闭当前连接，手动发送一个心跳包
//                ctx.channel().writeAndFlush(HEARTBEAT_DATA.duplicate());
                ctx.channel().writeAndFlush("Alive !!")
                println("成功向服务端发送心跳包....")
            } else {
                super.userEventTriggered(ctx, event)
            }
        }
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext) {
        println("正在与服务端建立连接....")
        // 建立连接成功之后，先向服务端发送一条数据
        ctx.channel().writeAndFlush("我是会发心跳包的客户端-A！")
        super.channelActive(ctx)
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext) {
        println(ctx.channel().toString() + ", 服务端主动关闭了连接....")
        super.channelInactive(ctx)
    }

    @Throws(Exception::class)
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // 处理异常
        if (cause is IOException && "远程主机强迫关闭了一个现有的连接。" == cause.message) {
            // 远程主机强制关闭连接异常处理逻辑
            // 在这里添加你的处理逻辑，例如记录日志、关闭连接等
            println("远程主机强制关闭了一个现有的连接。")
            ctx.close() // 关闭连接或其他操作
        } else {
            // 其他异常处理逻辑
            cause.printStackTrace()
            ctx.close() // 关闭连接或其他操作
        }
    }

    companion object {
        // 通用的心跳包数据
        private val HEARTBEAT_DATA: ByteBuf =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Alive !!", CharsetUtil.UTF_8))
    }
}
