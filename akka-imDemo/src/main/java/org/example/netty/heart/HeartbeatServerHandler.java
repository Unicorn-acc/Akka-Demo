package org.example.netty.heart;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;

// 心跳机制的服务端处理器
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        // 如果当前触发的事件是闲置事件
        if (event instanceof IdleStateEvent) {
            IdleStateEvent idleEvent = (IdleStateEvent) event;
            // 如果对应的Channel通道触发了读闲置事件
            if (idleEvent.state() == IdleState.READER_IDLE){
                // 表示对应的客户端没有发送心跳包，则关闭对应的网络连接
                // （心跳包也是一种特殊的数据，会触发读事件，有心跳就不会进这步）
                ctx.channel().close();
                System.out.println("关闭了未发送心跳包的连接....");
            } else {
                super.userEventTriggered(ctx, event);
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 如果收到的是心跳包，则给客户端做出一个回复
        if ("I am Alive".equals(msg)){
            ctx.channel().writeAndFlush("I know");
        }
        System.out.println("收到客户端消息：" + msg);
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 处理异常
        if (cause instanceof IOException && "远程主机强迫关闭了一个现有的连接。".equals(cause.getMessage())) {
            // 远程主机强制关闭连接异常处理逻辑
            // 在这里添加你的处理逻辑，例如记录日志、关闭连接等
            System.out.println(ctx.channel() + ", 远程主机强制关闭了一个现有的连接。");
        } else {
            // 其他异常处理逻辑
            cause.printStackTrace();
            ctx.close(); // 关闭连接或其他操作
        }
    }

}
