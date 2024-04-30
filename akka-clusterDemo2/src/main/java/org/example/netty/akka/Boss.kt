package org.example.netty.akka

import akka.actor.typed.ActorRef
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.*
import akka.actor.typed.pubsub.PubSub
import akka.actor.typed.pubsub.Topic
import akka.actor.typed.receptionist.ServiceKey
import io.netty.channel.Channel
import org.example.netty.msg.MyCommand
import org.example.netty.server.system
import java.time.Duration


class BossActor(context: ActorContext<MyCommand>, entityId: String) : AbstractBehavior<MyCommand>(context) {

    override fun createReceive(): Receive<MyCommand> {
        return newReceiveBuilder()
            .onMessage(MyCommand::class.java) { message: MyCommand -> this.onCommand(message) }
            .build()
    }

    private fun onCommand(message: MyCommand): Behavior<MyCommand> {
        when (message.action) {
            MyCommand.LOGIN -> {
                val username = message.username
                val channel = message.channel

                var workActorServiceKey : ServiceKey<String> = ServiceKey.create(String::class.java, username)

                val actorRef : ActorRef<String?>? = context.spawn(WorkActor.create(username, channel, pingServiceKey), username)
                actorRefMap[channel] = actorRef
                println("BossActor 用户 $username 登录, 当前人数：${actorRefMap.size}, actorRef: $actorRef, channel：$channel")
                actorRefMap[channel] = actorRef
                actorRef?.tell("doSend")

                // 订阅topic
                val pubSub = PubSub.get(system)
                var topic = pubSub.topic(String::class.java, "my-topic")
                topic.tell(Topic.subscribe(actorRef))

                // 当一个执行组件需要被另一个执行组件发现，但您无法在传入消息中引用该执行组件时，可以使用 Receptionist .它支持本地和集群（请参阅集群）context


            }
            MyCommand.LOGOUT -> {
                val username = message.username
                val channel = message.channel
                val actorRef: ActorRef<String?>? = actorRefMap.remove(channel)
                println("BossActor 用户 $username 退出, 当前人数：" + actorRefMap.size)

                // 取消订阅topic
                val pubSub = PubSub.get(system)
                var topic = pubSub.topic(String::class.java, "my-topic")
                topic.tell(Topic.unsubscribe(actorRef))

            }
            MyCommand.SEND -> {
                val username = message.username
                val channel = message.channel
                val actorRef: ActorRef<String?>? = actorRefMap[channel]
                println("BossActor 用户 $username 发送消息： ${message.msg}")
                actorRef?.tell("doSend")

                // 向频道发送消息
                val pubSub = PubSub.get(system)
                var topic = pubSub.topic(String::class.java, "my-topic")
                topic.tell(Topic.publish("用户 $username 发送消息： ${message.msg}"))
            }
            MyCommand.TellTo ->{
                val username = message.username
                val channel = message.channel
                val to = message.to
                println("BossActor 用户 $username 向用户 $to 私聊，内容： ${message.msg}")
//                val actorSelection =
//                    context.classicActorContext().actorSelection("akka://system/system/sharding/CounterCounter/561/%5Bid%3A+0xa911f66c%2C+L%3A%2F127.0.0.1%3A8085+-+R%3A%2F127.0.0.1%3A54747%5D/$to")
//                actorSelection.tell(message.msg, akka.actor.ActorRef.noSender())

//                context.system.receptionist().tell(Receptionist.find(pingServiceKey) { listing : Receptionist.Listing -> {
//                    listing.getServiceInstances(pingServiceKey).forEach { ref ->
//                        ref.tell("Hello from $username to $to: ${message.msg}")
//                    }
//                }})

//                val receptionist = system.receptionist()
//                var askTimeout : Duration = Duration.ofSeconds(3)
//                AskPattern.ask(
//                    receptionist,
//                    { replyTo -> receptionist.find(pingServiceKey, replyTo) },
//                    askTimeout,
//                    system.scheduler())
            }
        }
        return this
    }

    companion object {
        val actorRefMap: MutableMap<Channel, ActorRef<String?>?> = mutableMapOf()

        val pingServiceKey: ServiceKey<String> = ServiceKey.create(String::class.java, "pingService")

        fun create(entityId: String): Behavior<MyCommand> {
            return Behaviors.setup { ctx: ActorContext<MyCommand> ->


                BossActor(ctx, entityId)
            }
        }
    }
}