package com.netty.ex03;

import com.netty.ex01.EchoServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

//easy to change socket mode with a few code changes
public class EchoBlockingServer {

    private int port;

    public EchoBlockingServer(int port) {
        this.port = port;
    }
    public void run() throws Exception{
        EventLoopGroup bossGroup = new OioEventLoopGroup(1); // blokcing mode for socket mode (O for Old)
        EventLoopGroup workerGroup = new OioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(OioServerSocketChannel.class)  // 1
             .childHandler(new ChannelInitializer<SocketChannel>() { // 4        //자식 채널의 초기화 방법.
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new EchoServerHandler());
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
        new EchoBlockingServer(port).run();
    }
}
