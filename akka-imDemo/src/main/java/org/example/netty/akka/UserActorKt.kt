package org.example.netty.akka

import akka.actor.AbstractActor
import akka.actor.Props
import io.netty.channel.Channel
import org.example.netty.msg.BaseSendMsg

class UserActorKt(private val channel: Channel) : AbstractActor() {
    override fun createReceive(): Receive {
        return receiveBuilder() // 处理字符串类型的消息
            .match(String::class.java) { msg: String ->
                println("userActor 收到消息: $msg")
            }
            .match(BaseSendMsg::class.java) { sendMsg: BaseSendMsg ->
                println("parent: ${context.parent} --> $sendMsg --> ${self}")
                println("userActor 发送消息： ${sendMsg.msg}, 消息来自：${sendMsg.from}")
                val msg = sendMsg.from + " 说 ：" + sendMsg.msg
                channel.writeAndFlush(msg)
            }
            .build()
    }

    companion object {
        fun createProps(channel: Channel): Props {
            return Props.create(UserActorKt::class.java) { UserActorKt(channel) }
        }
    }
}
