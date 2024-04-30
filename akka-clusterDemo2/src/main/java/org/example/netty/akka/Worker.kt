package org.example.netty.akka

import akka.actor.ActorSelection
import akka.actor.typed.Behavior
import akka.actor.typed.javadsl.AbstractBehavior
import akka.actor.typed.javadsl.ActorContext
import akka.actor.typed.javadsl.Behaviors
import akka.actor.typed.javadsl.Receive
import akka.actor.typed.receptionist.Receptionist
import akka.actor.typed.receptionist.ServiceKey
import io.netty.channel.Channel
import org.example.netty.msg.MyCommand

class WorkActor(context: ActorContext<String?>?, private val name: String, private val channel: Channel) : AbstractBehavior<String>(context) {

    private var cnt = 0

    override fun createReceive(): Receive<String> {
        return newReceiveBuilder()
            .onMessage(String::class.java) { s: String -> this.onPrint(s) }
            .build()
    }

    private fun onPrint(s: String): Behavior<String?> {
        if(s.equals("doSend")){
            cnt += 1
            println(String.format("userActor 消息发送统计： %s, 总发送次数： %d", name, cnt))
            channel.writeAndFlush("（发送统计）用户$name, 你已发送消息次数：$cnt")
        }else{
            channel.writeAndFlush("（聊天）$s")
        }
        return this
    }

    companion object {
        fun create(name: String, channel: Channel, serviceKey: ServiceKey<String>): Behavior<String?> {

            return Behaviors.setup { ctx: ActorContext<String?>? ->

                ctx!!.system.receptionist().tell(Receptionist.register(serviceKey, ctx.self))

                WorkActor(ctx, name, channel)
            }
        }
    }
}
