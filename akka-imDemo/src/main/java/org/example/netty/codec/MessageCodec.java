package org.example.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.example.netty.message.dao.StrMessage;

import java.util.Arrays;
import java.util.List;

public class MessageCodec extends MessageToMessageCodec<ByteBuf, String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String s, List<Object> outList) throws Exception {
//        System.out.println("=====================");
//        System.out.println("encode : " + s);
        ByteBuf out = ctx.alloc().buffer();

        StrMessage.msg.Builder builder = StrMessage.msg.newBuilder();
        builder.setMsg(s);
        byte[] bytes = builder.build().toByteArray();

        out.writeInt(bytes.length);
        out.writeBytes(bytes);
//        System.out.println("encode result : " + Arrays.toString(bytes));
        outList.add(out);
//        System.out.println("=====================");
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        System.out.println("=====================");
        int length = in.readInt();
//        System.out.println("decode length : " + length);
        final byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length); // 读取进来，下面再进行 解码

        String msg = StrMessage.msg.parseFrom((byte[]) bytes).getMsg();
//        System.out.println("decode msg : " + msg);
        out.add(msg);
//        System.out.println("=====================");
    }
}
