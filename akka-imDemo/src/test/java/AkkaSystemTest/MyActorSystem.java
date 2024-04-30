package AkkaSystemTest;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.japi.Creator;
import io.netty.channel.Channel;
import org.example.netty.akka.UserActorKt;

public class MyActorSystem {

    public static void main(String[] args) {

        // 服务端启动， 创建ActorSystem
        // 服务端收到socket连接， 创建 roomActor(route) 和 UserActor(保存channel)
        // 服务端收到消息， 定位具体 roomActor，向roomActor发送消息， roomActor在收到消息后广播（channel.writeAndFlush()）

        //在创建MyActor实例之前，需要先创建一个ActorSystem实例，例如：
        ActorSystem system = ActorSystem.create("my-actor-system");


        //接着,我们可以使用ActorSystem实例创建MyActor实例：
        ActorRef myActor = system.actorOf(Props.create(MyActor.class), "my-actor");

        //上述代码使用Props类来创建一个MyActor实例，并将其注册到ActorSystem中，
        // 同时指定了MyActor实例的名称为my-actor。现在，我们可以向myActor发送消息，例如：
        myActor.tell("Hello, Actor!", ActorRef.noSender());

        //上述代码将一个类型为String、内容为"Hello, Actor!"的消息发送给myActor，同时指定了消息的发送者为ActorRef.noSender()，表示该消息是无发送者的。当myActor收到消息时，它会调用其createReceive方法中匹配类型为String的消息的行为，即打印收到的消息内容。

        // 关闭ActorSystem
        system.terminate();
    }
}

class GreeterMain extends AbstractBehavior<GreeterMain.SayHello> {

    public GreeterMain(ActorContext<SayHello> context) {
        super(context);
    }

    public static class SayHello {
        public final String name;

        public SayHello(String name) {
            this.name = name;
        }
    }

    @Override
    public Receive<SayHello> createReceive() {
        return newReceiveBuilder().onMessage(SayHello.class, this::onSayHello).build();
    }

    private Behavior<SayHello> onSayHello(SayHello command) {
        //#create-actors
//        akka.actor.typed.ActorRef<Greeter.Greeted> replyTo =
//                getContext().spawn(GreeterBot.create(3), command.name);
//        greeter.tell(new Greeter.Greet(command.name, replyTo));
        //#create-actors
        return this;
    }
}

class MyActor extends AbstractActor {
    public MyActor(Channel channel)
    {
        MyActor.channel = channel;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                // 处理字符串类型的消息
                .match(String.class, msg -> {
                    System.out.println("MyAcReceived message: " + msg);
                })
                .build();
    }

    public static Channel channel;

    public static Props createProps(Channel channel){
        return Props.create(new Creator<MyActor>() {
            @Override
            public MyActor create() throws Exception, Exception {
                return new MyActor(channel);
            }
        });
    }
}