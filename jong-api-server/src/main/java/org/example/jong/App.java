package org.example.jong;

import org.example.jong.config.ApiServerConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * Hello world!
 *
 */
public class App {



    public static void main( String[] args ) {
        AbstractApplicationContext springContext = null;

        try{
            springContext = new AnnotationConfigApplicationContext(ApiServerConfig.class);
            springContext.registerShutdownHook();

            ApiServer server = springContext.getBean(ApiServer.class);
            server.start();
        } finally {
            springContext.close();
        }

        System.out.println( "Hello World!" );
    }
}
