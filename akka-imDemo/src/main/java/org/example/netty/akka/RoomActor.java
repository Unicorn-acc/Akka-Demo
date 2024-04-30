package org.example.netty.akka;

import akka.actor.*;
import akka.routing.ActorRefRoutee;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Router;
import org.example.netty.msg.BaseSendMsg;
import org.example.netty.msg.CreateUserMsg;

public class RoomActor extends AbstractActor {

    private Router router;

    @Override
    public void preStart() throws Exception {
//        List<Routee> listRoutee=new ArrayList<Routee>();
//        for(int i=0; i<2; i++) {
//            ActorRef ref=getContext().actorOf(Props.create(RouteeActor.class), "routeeActor"+i);
//            listRoutee.add(new ActorRefRoutee(ref));
//        }

        /**
         * RoundRobinRoutingLogic: 轮询
         * BroadcastRoutingLogic: 广播
         * RandomRoutingLogic: 随机
         * SmallestMailboxRoutingLogic: 空闲
         */
        router=new Router(new BroadcastRoutingLogic());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 处理字符串类型的消息
                .match(String.class, msg -> {
                    System.out.println("roomActor 收到消息: " + msg);
                })
                // 创建用户的请求
                .match(CreateUserMsg.class, msg -> {
                    String name = msg.getName();
                    System.out.println("roomActor 聊天室用户加入：" + context().self() + ", name = " + name);
                    // 创建用户
                    ActorRef userActor = getContext().actorOf(Props.create(UserActor.class), name);
                    router = router.addRoutee(new ActorRefRoutee(userActor));

                    // 给用户发送欢迎登录
                    BaseSendMsg baseSendMsg = new BaseSendMsg("system", "欢迎登录", msg.getChannel());
                    userActor.tell(baseSendMsg, ActorRef.noSender());
                })
                .match(BaseSendMsg.class, msg -> {
                    System.out.println("roomActor 收到消息: " + msg.getMsg() + ", 消息来自：" + msg.getFrom());
                    // TODO 广播消息
                    BaseSendMsg baseSendMsg = new BaseSendMsg(msg.getFrom(), msg.getMsg(), msg.getChannel());
                    router.route(baseSendMsg, getSender());
                })
                .build();
    }

//    @Override
//    public void onReceive(Object message) throws Throwable {
//        router.route(message, getSender());
//    }
}
