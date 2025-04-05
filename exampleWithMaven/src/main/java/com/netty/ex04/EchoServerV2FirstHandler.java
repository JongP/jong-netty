package com.netty.ex04;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerV2FirstHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf in = (ByteBuf) msg;

        String message = in.toString(Charset.defaultCharset());
        System.out.print("first handler : "+ message);
        //System.out.flush();

        ctx.write(msg);
        //ctx.writeAndFlush(msg);

        ctx.fireChannelRead(msg); // 다음 핸들러로 메시지 전달

    }

}
