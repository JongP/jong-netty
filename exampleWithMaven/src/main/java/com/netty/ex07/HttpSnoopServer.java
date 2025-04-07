package com.netty.ex07;

import java.io.File;
import java.util.Scanner;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class HttpSnoopServer {
    private static final int PORT =8443;

    public static void main(String[] arg) throws Exception{
        SslContext sslContext = null;

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the password for the private key file:");
        String keyPwassword = sc.nextLine().trim();

        try{
            File certChainFile = new File("exampleWithMaven/openssl/netty.crt");
            File keyFile = new File("exampleWithMaven/openssl/privatekey.pem");

            System.out.println("Working directory: " + System.getProperty("user.dir"));


            if(certChainFile.exists() && keyFile.exists()){
                System.out.println("Certificate chain file exists.");
            }


            sslContext = SslContext.newServerContext(certChainFile, keyFile, keyPwassword);

        } catch(SSLException e){
            e.printStackTrace();
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpSnoopServerInitializer(sslContext));

            Channel ch = b.bind(PORT).sync().channel();
            ch.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
