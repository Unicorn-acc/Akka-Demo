package org.example.netty

import akka.actor.typed.ActorRef
import akka.actor.typed.ActorSystem
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.pubsub.PubSub
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import akka.cluster.sharding.typed.ShardingEnvelope
import akka.cluster.sharding.typed.javadsl.ClusterSharding
import akka.cluster.sharding.typed.javadsl.Entity
import akka.cluster.sharding.typed.javadsl.EntityRef
import akka.cluster.sharding.typed.javadsl.EntityTypeKey
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.timeout.IdleStateHandler
import org.example.netty.akka.BossActor
import org.example.netty.codec.MessageCodeckt
import org.example.netty.codec.ProcotolFrameDecoderkt
import org.example.netty.heart.HeartbeatServerHandlerkt
import org.example.netty.msg.MyCommand
import java.util.*
import java.util.concurrent.atomic.AtomicLong


object server {

    lateinit var system: ActorSystem<MyCommand?>


    @JvmStatic
    fun main(args: Array<String>) {

        system = ActorSystem.create(BossActor.create("1"), "system")

        val sharding = ClusterSharding.get(system)
        // 每个 Entity 类型都有一个键typeKey，然后用于检索给定实体标识符的 EntityRef。
        // Entity : 实体，就是分片管理的Actor
        val typeKey: EntityTypeKey<MyCommand> = EntityTypeKey.create(MyCommand::class.java, "CounterCounter")
        // init 应在每个节点上为每个实体类型调用集群分片。可以使用角色来控制在哪些节点上创建实体参与者。
        // init ShardRegion 将创建代理或ShardRegion，具体取决于节点的角色是否与实体的角色匹配。
        val shardRegion: ActorRef<ShardingEnvelope<MyCommand>> =
            sharding.init(Entity.of(typeKey) { ctx -> BossActor.create(ctx.getEntityId()) })

        val pingServiceKey: ServiceKey<String> = ServiceKey.create(String::class.java, "pingService")



        val nameMap: MutableMap<Channel, String> = mutableMapOf()
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

                        val numberId = counter.getAndIncrement()
                        val username = "username:$numberId"
                        nameMap.put(ctx.channel(), username)
                        var commend = MyCommand(ctx.channel(), username, MyCommand.LOGIN, "")

//                        system.tell(commend)
                        var entityId = ctx.channel().toString()
                        shardRegion.tell(ShardingEnvelope<MyCommand>(entityId, commend))
                    }

                    @Throws(Exception::class)
                    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
                        if(msg is String && !msg.startsWith("Alive")){
                            val userName = nameMap.get(ctx.channel())
                            var entityId = ctx.channel().toString()
                            if(!msg.startsWith("tell")){
                                var commend = userName?.let { MyCommand(ctx.channel(), it, MyCommand.SEND, msg) }
//                            system.tell(commend)
                                shardRegion.tell(ShardingEnvelope<MyCommand>(entityId, commend))
                            }else{
                                var toName = msg.split("\\")[1]
                                var commend = userName?.let { MyCommand(ctx.channel(), it, MyCommand.TellTo, "hello") }
                                commend!!.to = toName
                                shardRegion.tell(ShardingEnvelope<MyCommand>(entityId, commend))
                            }
                        }
                    }

                    @Throws(java.lang.Exception::class)
                    override fun channelInactive(ctx: ChannelHandlerContext) {
                        val userName = nameMap.get(ctx.channel())
                        var commend = userName?.let { MyCommand(ctx.channel(), it, MyCommand.LOGOUT, "") }

//                        system.tell(commend)
                        var entityId = ctx.channel().toString()
                        shardRegion.tell(ShardingEnvelope<MyCommand>(entityId, commend))
                    }

                    @Throws(java.lang.Exception::class)
                    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable?) {
                    }
                })
                ch.pipeline().addLast("HeartbeatHandler", HeartbeatServerHandlerkt());
            }
        })
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 绑定 8081 端口
        bootstrap.bind(8085)
    }
}
