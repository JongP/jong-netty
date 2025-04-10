package org.example.jong;


import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ApiServer {
    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress address;

    @Autowired
    @Qualifier("bossThreadCount")
    private int bossThreadCount;

    @Autowired
    @Qualifier("workerThreadCount")
    private int workerThreadCount;

    public void start(){
        System.out.println("Starting server...");
    }

}
