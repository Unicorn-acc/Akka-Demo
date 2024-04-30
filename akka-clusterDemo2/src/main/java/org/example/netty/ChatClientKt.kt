package org.example.netty

import io.netty.bootstrap.Bootstrap
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import org.example.netty.codec.MessageCodeckt
import org.example.netty.codec.ProcotolFrameDecoderkt
import org.example.netty.heart.HeartbeatClientHandlerkt
import java.util.*
import java.util.concurrent.TimeUnit

object client {
    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // 客户端引导器
        val bootstrap = Bootstrap()
        // 配置线程组
        bootstrap.group(NioEventLoopGroup())
        // 指定 IO 类型为 NIO
        bootstrap.channel(NioSocketChannel::class.java)
        // 配置 IO 处理器
        bootstrap.handler(object : ChannelInitializer<SocketChannel>() {
            @Throws(Exception::class)
            override fun initChannel(ch: SocketChannel) {

                ch.pipeline().addLast("IdleStateHandler", IdleStateHandler(0,5,0, TimeUnit.SECONDS));

                // 心跳处理、日志、编码解码器处理 MessageToMessageCodec
                ch.pipeline().addLast(ProcotolFrameDecoderkt()) // 帧解码器 【与自定义编解码器 MessageCodecSharable一起配置参数】
                ch.pipeline().addLast(MessageCodeckt())

                ch.pipeline().addLast(object: ChannelInboundHandlerAdapter() {
                    override fun channelActive(ctx: ChannelHandlerContext) {
                        println("连接建立")
                    }

                    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
                        println("收到消息：$msg")
                    }
                })
                ch.pipeline().addLast("HeartbeatHandler", HeartbeatClientHandlerkt());
            }
        })
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true) // 设置长连接
        // 建立连接
        val channel = bootstrap.connect("127.0.0.1", 8085).channel()
        channel.writeAndFlush("hello world..")

        // 发送消息
        Thread {
            val scanner = Scanner(System.`in`)
            while (scanner.hasNextLine()) {
                val message = scanner.nextLine()
                channel.writeAndFlush(message)
            }
        }.start()

        // 关闭连接
        channel.closeFuture().sync()
    }
}
