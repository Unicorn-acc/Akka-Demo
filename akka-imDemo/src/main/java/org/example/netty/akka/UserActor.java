package org.example.netty.akka;

import akka.actor.AbstractActor;
import org.example.netty.msg.BaseSendMsg;

public class UserActor extends AbstractActor {


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 处理字符串类型的消息
                .match(String.class, msg -> {
                    System.out.println("userActor 收到消息: " + msg);
                })
                .match(BaseSendMsg.class, sendMsg -> {
                    System.out.println("userActor 发送消息：" + sendMsg.getMsg() + ", 消息来自：" + sendMsg.getFrom());
                    String msg = sendMsg.getFrom() + " 说 ：" + sendMsg.getMsg();
                    sendMsg.getChannel().writeAndFlush(msg);
                })
                .build();
    }
}
