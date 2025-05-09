package com.netty.ex02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;

public class NonBlockingServer {
    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    private ByteBuffer buffer = ByteBuffer.allocate(2*1024);

    private void startEchoServer(){
        try(
                Selector selector = Selector.open();
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                ){
            if(serverSocketChannel.isOpen() && selector.isOpen()){
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(new InetSocketAddress(8888));

                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                System.out.println("접속 대기 중");


                while(true){
                    selector.select();
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                    while(keys.hasNext()){
                        SelectionKey key = (SelectionKey) keys.next();
                        keys.remove();

                        if(!key.isValid()){
                            continue;
                        }

                        if(key.isAcceptable()){
                            this.acceptOP(key,selector);
                        } else if(key.isReadable()){
                            this.readOP(key);
                        } else if(key.isWritable()){
                            this.writeOP(key);
                        }else{
                            System.out.println("failed to make server socket");
                        }
                    }
                }

            }

        }catch (IOException ex){
            System.err.println(ex);
        }
    }

    private void acceptOP(SelectionKey key, Selector selector) throws IOException{
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverChannel.accept();
        socketChannel.configureBlocking(false);

        System.out.println("connected client: "+socketChannel.getRemoteAddress());

        keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
        socketChannel.register(selector,SelectionKey.OP_READ);
    }

    private void readOP(SelectionKey key) {
        try{
            SocketChannel socketChannel = (SocketChannel)  key.channel();
            buffer.clear();
            int numRead=-1;
            try{
                numRead = socketChannel.read(buffer);
            } catch (IOException ex){
                System.err.println("failed to read data");
            }

            if(numRead==-1){
                this.keepDataTrack.remove(socketChannel);
                System.out.println("complete client connection : "+socketChannel.getRemoteAddress());
                socketChannel.close();
                key.cancel();
                return;
            }

            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            System.out.println(new String(data, "UTF-8") + " from " + socketChannel.getRemoteAddress());

            doEchoJob(key,data);
        } catch (IOException ex){
            System.err.println(ex);
        }

    }

    private void doEchoJob(SelectionKey key, byte[] data) {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        channelData.add(data);

        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void writeOP(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel(); // accept 될 때까지 블록킹

        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        Iterator<byte[]> its = channelData.iterator();

        while(its.hasNext()){
            byte[] it = its.next();
            its.remove();
            socketChannel.write(ByteBuffer.wrap(it));
        }

        key.interestOps(SelectionKey.OP_READ);
    }

    public static void main(String[] arg){
        NonBlockingServer server = new NonBlockingServer();
        server.startEchoServer();
    }

}
