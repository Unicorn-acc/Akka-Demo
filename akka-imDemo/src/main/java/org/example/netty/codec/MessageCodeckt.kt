package org.example.netty.codec

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import org.example.netty.message.dao.StrMessage.msg


class MessageCodeckt : MessageToMessageCodec<ByteBuf, String?>() {
    @Throws(Exception::class)
    override fun encode(ctx: ChannelHandlerContext, s: String?, outList: MutableList<Any>) {
//        System.out.println("=====================");
//        System.out.println("encode : " + s);
        val out = ctx.alloc().buffer()

        val builder = msg.newBuilder()
        builder.setMsg(s)
        val bytes = builder.build().toByteArray()

        out.writeInt(bytes.size)
        out.writeBytes(bytes)
        //        System.out.println("encode result : " + Arrays.toString(bytes));
        outList.add(out)
        //        System.out.println("=====================");
    }

    @Throws(Exception::class)
    override fun decode(ctx: ChannelHandlerContext, inn : ByteBuf, out: MutableList<Any>) {
//        System.out.println("=====================");
        val length = inn.readInt()
        //        System.out.println("decode length : " + length);
        val bytes = ByteArray(length)
        inn.readBytes(bytes, 0, length) // 读取进来，下面再进行 解码

        val msg = msg.parseFrom(bytes).msg
        //        System.out.println("decode msg : " + msg);
        out.add(msg)
        //        System.out.println("=====================");
    }
}