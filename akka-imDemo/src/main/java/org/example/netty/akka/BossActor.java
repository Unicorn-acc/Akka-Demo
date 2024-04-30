package org.example.netty.akka;

import akka.actor.*;
import akka.japi.Creator;
import io.netty.channel.Channel;
import org.example.netty.msg.BaseSendMsg;
import org.example.netty.msg.CreateUserMsg;
import org.example.netty.msg.MsgRouterMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建actor（调用context.spawn()方法）是需要ActorContext信息的，而这个ActorContext是不能被拿到akka环境外使用的。
 *                  也就是说，不能从service里面直接去调用context.spawn()来创建actor。
 * 解决方案：
 * 在ActorSystem启动时，生成一个用来创建actor的root actor。
 * 我们把这个root actor的ActorRef放到系统的公共变量里，然后通过给这个actor发消息来实现对ActorSystem内actor的管理。
 *
 */
public class BossActor extends AbstractActor {

    private static final Map<Channel, String> nameMap = new HashMap<>();


    public static Props props() {
        return Props.create(new Creator<BossActor>() {
            @Override
            public BossActor create() throws Exception {
                System.out.println("bossActor 已创建");
                return new BossActor();
            }
        });
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 处理字符串类型的消息
                .match(String.class, msg -> {
                    System.out.println("bossActor 收到消息: " + msg);
                })
                // 创建用户
                .match(CreateUserMsg.class, msg -> {
                    String name = msg.getName();
                    String roomid = "1";
                    System.out.println("bossActor 创建用户处理, 用户：" + name + ", 聊天室：" + roomid);

                    nameMap.put(msg.getChannel(), msg.getName());

                    CreateUserMsg createUserMsg = new CreateUserMsg();
                    createUserMsg.setName(name);
                    createUserMsg.setChannel(msg.getChannel());

                    // 判断聊天室路由是否存在

                    ActorSelection actorSelection = context().actorSelection("akka://ChatServer/user/BossActor/chatRoom:1");
                    actorSelection.tell(new Identify("A001"), getSender());

                    // 创建聊天室路由 RoomActor， 将用户创建的任务交给聊天室， 由对应roomActor进行消息转发，
                    ActorRef routerActor = getContext().actorOf(Props.create(RoomActor.class), "chatRoom:" + roomid);
                    routerActor.tell(createUserMsg, ActorRef.noSender());

                })
                // 消息发送
                // TODO 根据不同的 聊天室id ，进行消息路由
                .match(MsgRouterMsg.class, msg -> {
                    String name = nameMap.get(msg.getChannel());
                    String roomid = "1";
                    System.out.println("bossActor 消息路由处理, " + "用户：" + name + ", 聊天室：" + roomid + ", 发送消息：" + msg.getMsg());

                    // 给路由actor 发送消息，路由负责将消息转发给聊天室所有人
//                    System.out.println(getContext().lookupRoot());
                    ActorSelection roomActor = getContext().actorSelection("akka://ChatServer/user/BossActor/chatRoom:" + roomid);
                    BaseSendMsg baseSendMsg = new BaseSendMsg(name, msg.getMsg(), msg.getChannel());
                    roomActor.tell(baseSendMsg, ActorRef.noSender());
                })
                .build();
    }
}
