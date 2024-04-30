package org.example.netty

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import org.example.netty.akka.BossActorKt
import org.example.netty.codec.MessageCodeckt
import org.example.netty.codec.ProcotolFrameDecoderkt
import org.example.netty.heart.HeartbeatServerHandler
import org.example.netty.heart.HeartbeatServerHandlerkt
import org.example.netty.msg.CreateUserMsg
import org.example.netty.msg.MsgRouterMsg
import org.example.netty.msg.RemoveUserMsg
import java.util.*
import java.util.concurrent.atomic.AtomicLong

object ChatServerKt {

    lateinit var system: ActorSystem

    @JvmStatic
    fun main(args: Array<String>) {

        val actorSystem = ActorSystem.create("ChatServerKt")

        val bossActor = actorSystem.actorOf(Props.create(BossActorKt::class.java), "BossActor")

        val counter = AtomicLong()

        // 保存已经注册的用户
        val Channellist = mutableListOf<Channel>()

        // 创建服务端启动引导器
        val bootstrap = ServerBootstrap()
        // 配置线程模型
        bootstrap.group(NioEventLoopGroup())
        // 指定服务端的 IO 模型
        bootstrap.channel(NioServerSocketChannel::class.java)
        // 定义处理器 Handler
        bootstrap.childHandler(object : ChannelInitializer<NioSocketChannel>() {
            @Throws(Exception::class)
            override fun initChannel(ch: NioSocketChannel) {

                // 用来判断 是不是读 空闲时间过长，或写空闲时间过长 (读，写，读写空闲时间限制) 0表示不关心
                ch.pipeline().addLast(IdleStateHandler(12, 0, 0))

                // 心跳处理、日志、编码解码器处理 MessageToMessageCodec
                ch.pipeline().addLast(ProcotolFrameDecoderkt()) // 帧解码器 【与自定义编解码器 MessageCodecSharable一起配置参数】
                ch.pipeline().addLast(MessageCodeckt())

                ch.pipeline().addLast(object : ChannelInboundHandlerAdapter() {
                    @Throws(Exception::class)
                    override fun channelActive(ctx: ChannelHandlerContext) {
                        println(ctx.channel().toString() + "登录")
                        Channellist.add(ctx.channel())
                        println("当前用户数：" + Channellist.size)

                        val numberId = counter.getAndIncrement()
                        val username = "username:$numberId"


                        // 创建 UserActor (向bossActor发送消息，由bossActor负责创建和管理userActor)
                        val createUserMsg = CreateUserMsg()
                        createUserMsg.name = username
                        createUserMsg.roomid = "1"
                        createUserMsg.channel = ctx.channel()
                        bossActor.tell(createUserMsg, ActorRef.noSender())
                    }

                    @Throws(Exception::class)
                    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                        if (msg is String && !msg.startsWith("Alive")) {
                            val msgRouterMsg = MsgRouterMsg()
                            msgRouterMsg.channel = ctx.channel()
                            msgRouterMsg.msg = msg as String
                            msgRouterMsg.toRoom = "1"
                            bossActor.tell(msgRouterMsg, ActorRef.noSender())
                        }

                    }

                    @Throws(java.lang.Exception::class)
                    override fun channelInactive(ctx: ChannelHandlerContext) {
                        println(ctx.channel().toString() + "退出")
                        Channellist.remove(ctx.channel())
                        println("当前用户数：" + Channellist.size)

                        var msg = RemoveUserMsg()
                        msg.channel = ctx.channel()
                        bossActor.tell(msg, ActorRef.noSender())
                    }

                    @Throws(java.lang.Exception::class)
                    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
                        println(ctx.channel().toString() + "异常退出")
                        Channellist.remove(ctx.channel())
                        println("当前用户数：" + Channellist.size)
                    }
                })
                ch.pipeline().addLast("HeartbeatHandler", HeartbeatServerHandlerkt());
            }
        })
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 绑定 8081 端口
        bootstrap.bind(8081)
    }
}
