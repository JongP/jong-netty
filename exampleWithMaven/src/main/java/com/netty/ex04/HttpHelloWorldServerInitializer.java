package com.netty.ex04;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

public class HttpHelloWorldServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;
    public HttpHelloWorldServerInitializer(SslContext sslCtx) {
        this.sslCtx= sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        if(sslCtx!=null){
            p.addLast(sslCtx.newHandler(socketChannel.alloc()));
        }

        p.addLast(new HttpServerCodec());
        p.addLast(new HttpHelloWorldServerHandler());
    }
}
