//package org.example.demo
//
//import akka.actor.AbstractActor
//import akka.actor.ActorRef
//import akka.actor.ActorSystem
//import akka.actor.Props
//import akka.cluster.sharding.ClusterSharding
//import akka.cluster.sharding.ClusterShardingSettings
//import akka.cluster.sharding.ShardCoordinator
//import akka.cluster.sharding.ShardRegion
//import akka.cluster.sharding.`ShardRegion$`
//import akka.japi.Option
//import akka.japi.pf.ReceiveBuilder
//import com.typesafe.config.ConfigFactory
//import org.slf4j.LoggerFactory
//import java.io.Serializable
//import java.time.Clock.system
//import java.time.Clock.system
//import java.util.*
//import java.time.Clock.system
//
//class shardRegionDemo {
//
//    fun main() {
//
//        shardRegProxy()
//        startShardRegion(2661)
//        startShardRegion(2662)
//
//    }
//
//
//}
//
//fun shardRegProxy() {
//
//    var actorSystem = createActorSystem(2663)
//
//    //startProxy 代理模式,即它不会承载任何实体本身，但知道如何将消息委托到正确的位置
//    ClusterSharding.get(actorSystem)
//        .startProxy("dogShard", Optional.empty(), ShardExtractor())
//        .let { println("  shard proxy $it started.") }
//
//    Thread.sleep(3000)
//
//    var shardReg = ClusterSharding.get(actorSystem).shardRegion("dogShard")
//
//
//    for (i in 1..100) {
//
//        shardReg.tell(DogMsg(i, "C wang"), ActorRef.noSender())
//
//        Thread.sleep(1500)
//    }
//}
//
//fun startShardRegion(port: Int) {
//
//
//    var actorSystem = createActorSystem(port)
//
//    val settings = ClusterShardingSettings.create(actorSystem)//.withRole("ClusterShardRole")
//
//    val shardReg = ClusterSharding.get(actorSystem).start(
//        "dogShard",
//        Props.create(DogActor::class.java),
//        settings,
//        ShardExtractor(),
//        ShardCoordinator.LeastShardAllocationStrategy(10, 1),
//        handOffStopMessage
//    )
//
//    for (i in 1..10) {
//
//        shardReg.tell(DogMsg(i, " wang"), ActorRef.noSender())
//
//        Thread.sleep(3000)
//    }
//
//}
//
//
//
//
////分区停止时会派发的消息类型
//object handOffStopMessage
//
//fun createActorSystem(port: Int): ActorSystem {
//    val config = ConfigFactory.parseString(
//        "akka.remote.artery.canonical.port=$port"
//    ).withFallback(
//        ConfigFactory.load()
//    )
//
//    var actorSystem = ActorSystem.create("custerA", config);
//
//    return actorSystem
//
//}
//
////分片规则
//class ShardExtractor : `ShardRegion$`.MessageExtractor {
//    //提取实体ID，实体对应的actor
//    override fun entityId(message: Any?): String {
//        if (message is DogMsg) {
//            return message.id.toString()
//        } else {
//            throw RuntimeException("无法识别消息类型 $message")
//        }
//    }
//
//    //根据实体ID，计算出对应分片ID
//    override fun shardId(message: Any?): String {
//        //var numberOfShards: Int = 10 //简单的分区数取模 return message.id%numberOfShards
//        if (message is DogMsg) {
//            //return (message.id % 10).toString()
//            return message.id.toString()
//        } else {
//            throw RuntimeException("无法识别消息类型 $message")
//        }
//    }
//
//    //对消息可进行拆封操作
//    override fun entityMessage(message: Any): Any {
//        return message
//    }
//
//}
//
////分布到集群环境中
//class DogActor : AbstractActor() {
//
//    var log = LoggerFactory.getLogger(DogActor::class.java)
//
//    override fun createReceive(): Receive {
//        return ReceiveBuilder.create().matchAny(this::receive).build()
//    }
//
//    fun receive(obj: Any) {
//
//        log.info("收到消息: $obj")
//        if (obj is DogMsg) {
//            log.info("${obj.id} ${obj.msg}")
//        }
//
//    }
//
//}
//
////定义消息（必须带有实体ID进行分片）
//data class DogMsg(var id: Int, var msg: String) : Serializable
