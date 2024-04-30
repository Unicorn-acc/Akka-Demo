package AkkaSystemTest;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.netty.codec.MessageCodec;
import org.example.netty.codec.ProcotolFrameDecoderkt;

import java.util.Scanner;

public class client {
    public static void main(String[] args) throws InterruptedException {

//        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);

        // 客户端引导器
        Bootstrap bootstrap = new Bootstrap();
        // 配置线程组
        bootstrap.group(new NioEventLoopGroup());
        // 指定 IO 类型为 NIO
        bootstrap.channel(NioSocketChannel.class);
        // 配置 IO 处理器
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 配置如果3s内未触发写事件，就会触发写闲置事件
//                ch.pipeline().addLast("IdleStateHandler",
//                        new IdleStateHandler(0,3,0, TimeUnit.SECONDS));
//                ch.pipeline().addLast(LOGGING_HANDLER);

                // 心跳处理、日志、编码解码器处理 MessageToMessageCodec
                ch.pipeline().addLast(new ProcotolFrameDecoderkt()); // 帧解码器 【与自定义编解码器 MessageCodecSharable一起配置参数】
                ch.pipeline().addLast(new MessageCodec());

                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {

                    // 在连接建立后触发 active事件
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        // 可以开启线程进行用户的登录操作
                        // 登录成功，另起线程：菜单里进行 收发消息操作
                        System.out.println("连接建立");

                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
                        System.out.println("收到消息：" + msg);
                    }
                });

                // 装载自定义的客户端心跳处理器
//                ch.pipeline().addLast("HeartbeatHandler",new HeartbeatClientHandler());
            }
        });
        // 建立连接
        Channel channel = bootstrap.connect("127.0.0.1",8081).sync().channel();
        channel.writeAndFlush("hello world..");
        // 发送消息
        new Thread(() ->{
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                channel.writeAndFlush(message);
            }
        }).start();

        // 关闭连接
        channel.closeFuture().sync();
    }
}
