package AkkaSystemTest


import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.japi.pf.ReceiveBuilder
import akka.routing.*
import org.slf4j.LoggerFactory

/**
 * Created by: tankx
 * Date: 2019/7/20
 * Description: 路由示例
 */
class WorkActor : AbstractActor() {
    var log = LoggerFactory.getLogger(WorkActor::class.java)

    override fun createReceive(): Receive {
        return ReceiveBuilder.create().matchAny(this::receive).build()
    }


    fun receive(msg: Any) {
        log.info(" {}: $msg", self.path())
    }
}

class RouterActor : AbstractActor() {

    var log = LoggerFactory.getLogger(RouterActor::class.java)

    private lateinit var router: Router;

    override fun preStart() {
        super.preStart()
        var list = arrayListOf<Routee>()

        for (i in 1..10) {
            var worker = context.actorOf(Props.create(WorkActor::class.java), "worker_$i")
            list.add(ActorRefRoutee(worker))
        }

        /**
         * 路由方式
         * RoundRobinRoutingLogic: 轮询
         * BroadcastRoutingLogic: 广播
         * RandomRoutingLogic: 随机
         * SmallestMailboxRoutingLogic: 空闲
         */
        router = Router(BroadcastRoutingLogic(), list)

    }

    override fun createReceive(): Receive {
        return ReceiveBuilder.create().matchAny(this::receive).build()
    }


    fun receive(msg: Any) {
        //log.info("RouterActor : $msg")
        router.route(msg, sender)
    }

}

fun main() {

    var actorSystem = ActorSystem.create("RouterSystem")

    var routerActor = actorSystem.actorOf(Props.create(RouterActor::class.java))

    for (i in 1..20) {

        Thread.sleep(2000)

        routerActor.tell("消息来了", ActorRef.noSender())
    }

}