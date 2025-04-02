package com.netty.ex02;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BlockingServer {
    public static void main(String[] args) throws Exception{
        BlockingServer server = new BlockingServer();
        server.run();
    }

    private void run() throws IOException {
        ServerSocket server = new ServerSocket(8888);
        System.out.println("Listening for connection on port 8888 ...");

        while (true) {
            Socket socket = server.accept(); //연결 될 때 까지 block. 소켓 생성마다 thread가 block되기 때문에, 입출력을 다른 thread에게 맞긴다.
            System.out.println("Connected to " + socket);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            while(true){
                try {
                    int request = in.read();  //read할 때 까지 block
                    out.write(request);  // OS 송신 버퍼가 모두 채워지고 비워질 때까지 block
                } catch (IOException e){
                    break;
                }
            }
        }
        //소켓 생성마다 thread가 block되기 때문에, 입출력을 다른 thread에게 맞긴다. thread pool로 관리 가능
        //한 번에 여러 사용자 못받음
        ////accept() 메소드도 blocking
        ////thread 계속 늘릴 수 없음

    }
}
