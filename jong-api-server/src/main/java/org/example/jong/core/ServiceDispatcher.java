package org.example.jong.core;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServiceDispatcher {
    private static final String NOT_FOUND_BEAN_NAME = "notFound";
    private static ApplicationContext applicationContext;

    @Autowired
    public void init(ApplicationContext applicationContext) {
        ServiceDispatcher.applicationContext = applicationContext;
    }

    protected Logger logger = LogManager.getLogger(this.getClass());

    public static ApiRequest dispatch(Map<String,String> requestMap){
        String serviceUri = requestMap.get("REQUEST_URI");
        String beanName = null;

        if(serviceUri == null) {
            beanName = NOT_FOUND_BEAN_NAME;
        }

        if (serviceUri.startsWith("/tokens")){
            String httpMethod = requestMap.get("REQUEST_METHOD");

            switch (httpMethod){
                case "POST":
                    beanName = "tokenIssue";
                    break;
                case "DELETE":
                    beanName = "tokenExpire";
                    break;
                case "GET":
                    beanName = "tokenVerify";
                    break;
                default:
                    beanName = NOT_FOUND_BEAN_NAME;
                    break;
            }
        } else if(serviceUri.startsWith("/users")){
            beanName = "users";
        } else {
            beanName= NOT_FOUND_BEAN_NAME;
        }

        ApiRequest service = null;

        try{
            service = (ApiRequest) applicationContext.getBean(beanName, requestMap);
        } catch (Exception e) {
            e.printStackTrace();
            service = (ApiRequest) applicationContext.getBean(NOT_FOUND_BEAN_NAME, requestMap);
        }

        return service;
    }
}

