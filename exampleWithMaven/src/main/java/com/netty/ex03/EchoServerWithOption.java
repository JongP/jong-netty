package com.netty.ex03;

import com.netty.ex01.EchoServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServerWithOption {

    private int port;

    public EchoServerWithOption(int port) {
        this.port = port;
    }
    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)                                             //thread group
                    .channel(NioServerSocketChannel.class)  // 1                        //network IO mode setting for server socket, parent thread - NIO mode
                    .option(ChannelOption.SO_BACKLOG,1) // 2                      //server socket option (same with JVM). SO_BACKLOG server sockt에 동시 연결 요청 갯수
                    .childOption(ChannelOption.SO_LINGER,0)
             .handler(new LoggingHandler(LogLevel.INFO)) // 3                    //event handler ofr server socket
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 4        //자식 채널의 초기화 방법.
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoServerHandler()); //1

                        }
                    });

                    //.option(ChannelOption.SO_BACKLOG, 128)
                    //.childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync(); // 2
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 8080;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        new EchoServerWithOption(port).run();
    }
}
//결론적으로, 네트워크 IO에 부담이 많은 어플리케이션을 NIO로 가야, 성능 향상이 크다.
