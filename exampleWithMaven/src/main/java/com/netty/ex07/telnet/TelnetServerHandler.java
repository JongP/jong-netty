package com.netty.ex07.telnet;

import java.net.InetAddress;
import java.util.Date;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Sharable       //공유 가능 상태표시 어노테이션. 채널 파이프라인에서 공유 가능. (다중 쓰레드에서 경합 없이 참조 가능)
public class TelnetServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("Connected to "+InetAddress.getLocalHost().getHostName()+"\r\n");
        ctx.write("current time: "+ new Date() +"\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String request) throws Exception {
        String response;
        boolean close = false;

        if(request.isEmpty()){
            response = "Please enter a command.\r\n";
        }
        else if ("bye".equals(request.toLowerCase())){
            response = "goodbye\r\n";
            close = true;
        }
        else {
            response = "the request is "+request+"\r\n";
        }

        ChannelFuture future = channelHandlerContext.write(response);

        if(close){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable ex){
        ex.printStackTrace();
        ctx.close();
    }
}
