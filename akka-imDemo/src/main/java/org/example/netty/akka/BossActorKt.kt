package org.example.netty.akka

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akka.event.Logging
import akka.event.LoggingAdapter
import akka.routing.ActorRefRoutee
import akka.routing.BroadcastRoutingLogic
import akka.routing.Router
import io.netty.channel.Channel
import org.example.netty.msg.BaseSendMsg
import org.example.netty.msg.CreateUserMsg
import org.example.netty.msg.MsgRouterMsg
import org.example.netty.msg.RemoveUserMsg


class BossActorKt : AbstractActor() {

    private val log: LoggingAdapter = Logging.getLogger(context.system, this)

    override fun createReceive(): Receive {
        return receiveBuilder() // 处理字符串类型的消息
            .match(String::class.java) { msg: String ->
                println("bossActor 收到消息: $msg")
            } // 创建用户
            .match(CreateUserMsg::class.java) { msg: CreateUserMsg ->
                val name = msg.name!!
                val roomid = msg.roomid!!
                val channel = msg.channel!!
                println("bossActor 创建用户处理, 用户：$name, 聊天室：$roomid")

                // 创建用户UserActor，得到actorRef
                val userActor = context.actorOf(UserActorKt.createProps(channel), name)
                // 保存用户名
                nameMap[channel] = name
                actorMap[channel] = userActor

                // 保存用户和聊天室的映射关系
                if(!userToRoomMap.contains(name)){
                    userToRoomMap[name] = mutableSetOf()
                }
                if(!roomToUserMap.contains(roomid)){
                    roomToUserMap[roomid] = HashSet<String>()
                    roomToUserRouter[roomid] = Router(BroadcastRoutingLogic());
                }
                var router = roomToUserRouter[roomid]!!
                router = router.addRoutee(ActorRefRoutee(userActor))
                roomToUserRouter.remove(roomid)
                roomToUserRouter[roomid] = router
                roomToUserMap[roomid]!!.add(name)
                userToRoomMap[name]!!.add(roomid)

                // 给用户发送欢迎登录
                val baseSendMsg = BaseSendMsg("system", "欢迎登录", msg.channel)
                userActor.tell(baseSendMsg, sender)
                println("聊天室：$roomid 人数：${roomToUserMap[roomid]!!.size}")
            } // 用户发送消息
            .match(MsgRouterMsg::class.java) { msg: MsgRouterMsg ->
                val name = nameMap[msg.channel]
                val roomid = msg.toRoom
                println("bossActor 消息路由处理, " + "用户： $name, 聊天室： $roomid, 发送消息： ${msg.msg}")
                println("router: ${roomToUserRouter[roomid]!!}")
                val baseSendMsg = BaseSendMsg(name, msg.msg, msg.channel)
                roomToUserRouter[roomid]!!.route(baseSendMsg, sender)

            } // 用户退出消息
            .match(RemoveUserMsg::class.java) { msg: RemoveUserMsg ->
                val name = nameMap[msg.channel]
                println("bossActor 用户退出处理， 用户：$name")
                nameMap.remove(msg.channel)
                val userActorRef = actorMap.remove(msg.channel)
                val rooms = userToRoomMap.remove(name)
                if (rooms != null) {
                    for(roomId in rooms){
                        roomToUserMap[roomId]!!.remove(name)

                        var router = roomToUserRouter[roomId]!!
                        router = router.removeRoutee(ActorRefRoutee(userActorRef))
                        roomToUserRouter[roomId] = router
                        // 发送用户退出消息
                        val baseSendMsg = BaseSendMsg("system", "用户 $name 退出", msg.channel)
                        router.route(baseSendMsg, sender)

                        println("聊天室：$roomId 人数：${roomToUserMap[roomId]!!.size}")
                    }
                }
            }
            .build()
    }

    companion object {
        private val nameMap: MutableMap<Channel, String> = mutableMapOf() // channel -> name
        private val actorMap: MutableMap<Channel, ActorRef> = mutableMapOf() // channel -> actorRef
        private val userToRoomMap: MutableMap<String, MutableSet<String>> = mutableMapOf() // user -> roomIds
        private val roomToUserMap: MutableMap<String, MutableSet<String>> = mutableMapOf() // roomId -> users
        private val roomToUserRouter: MutableMap<String, Router> = mutableMapOf() // roomId -> router

        fun props(): Props {
            return Props.create {
                println("bossActor 已创建")
                BossActor()
            }
        }
    }
}
