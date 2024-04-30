package org.example.netty;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.netty.akka.BossActor;
import org.example.netty.codec.MessageCodec;
import org.example.netty.codec.ProcotolFrameDecoderkt;
import org.example.netty.msg.CreateUserMsg;
import org.example.netty.msg.MsgRouterMsg;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ChatServer {

    public static class systemBehavior{
        static Behavior<Void> create() {
            return Behaviors.empty();
        }
    }

    public static void main(String[] args) {

//        final ActorSystem<Void> actorSystem = ActorSystem.create(systemBehavior.create(), "ChatServer");
        ActorSystem actorSystem = ActorSystem.create("ChatServer");

        ActorRef bossActor = actorSystem.actorOf(Props.create(BossActor.class), "BossActor");

        AtomicLong counter = new AtomicLong();

        // 保存已经注册的用户
        List<Channel> Channellist = new ArrayList<Channel>();

//        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

        // 创建服务端启动引导器
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 配置线程模型
        bootstrap.group(new NioEventLoopGroup());
        // 指定服务端的 IO 模型
        bootstrap.channel(NioServerSocketChannel.class);
        // 定义处理器 Handler
        bootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                // 配置如果5s内未触发读事件，就会触发读闲置事件
//                ch.pipeline().addLast("IdleStateHandler",
//                        new IdleStateHandler(5,0,0, TimeUnit.SECONDS));
//                ch.pipeline().addLast(LOGGING_HANDLER);

                ch.pipeline().addLast(new ProcotolFrameDecoderkt()); // 帧解码器 【与自定义编解码器 MessageCodecSharable一起配置参数】
                ch.pipeline().addLast(new MessageCodec());

                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println(ctx.channel() + "登录");
                        Channellist.add(ctx.channel());
                        System.out.println("当前用户数：" + Channellist.size());

                        long numberId = counter.getAndIncrement();
                        String username = "username:" + numberId;

                        // 创建 UserActor (向bossActor发送消息，由bossActor负责创建和管理)
                        CreateUserMsg createUserMsg = new CreateUserMsg();
                        createUserMsg.setName(username);
                        createUserMsg.setChannel(ctx.channel());
                        bossActor.tell(createUserMsg, ActorRef.noSender());


                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                        System.out.println(ctx.channel() + "，发送消息：" + msg);
//                        if(msg instanceof String && !((String) msg).startsWith("Alive")){
//                            System.out.println("进行消息转发");
//                            for(Channel channel : Channellist){
//                                channel.writeAndFlush(msg);
//                                System.out.println("转发给：" + channel);
//                            }
//                        }

                        // 向 userActor 发送消息
                        // 1. 给 bossActor 发送消息
                        // 2. bossActor 给 roomActor 发送消息
                        // 3. roomActor 给 userActor 广播路由消息
                        if(msg instanceof String && !((String) msg).startsWith("Alive")){
                            MsgRouterMsg msgRouterMsg = new MsgRouterMsg();
                            msgRouterMsg.setChannel(ctx.channel());
                            msgRouterMsg.setMsg((String)msg);
                            bossActor.tell(msgRouterMsg, ActorRef.noSender());
                        }

                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
                        System.out.println(ctx.channel() + "退出");
                        Channellist.remove(ctx.channel());
                        System.out.println("当前用户数：" + Channellist.size());
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        System.out.println(ctx.channel() + "异常:" + cause.getLocalizedMessage());
                        Channellist.remove(ctx.channel());
                        System.out.println("当前用户数：" + Channellist.size());
                    }

                });
                // 装载自定义的服务端心跳处理器
//                ch.pipeline().addLast("HeartbeatHandler",new HeartbeatServerHandler());
            }
        });
        // 绑定 8081 端口
        bootstrap.bind(8081);
    }

}
