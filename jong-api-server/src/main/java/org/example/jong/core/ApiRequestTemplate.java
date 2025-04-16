package org.example.jong.core;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.jong.service.exception.RequestParamException;
import org.example.jong.service.exception.ServiceException;

import com.google.gson.JsonObject;

public abstract class ApiRequestTemplate implements ApiRequest{
    protected Logger logger;

    protected Map<String, String> reqData;

    protected JsonObject apiResult;


    public ApiRequestTemplate(Map<String, String> reqData) {
        this.logger = LogManager.getLogger(this.getClass());
        this.apiResult = new JsonObject();
        this.reqData = reqData;

        logger.info("request data: "+this.reqData);
    }

    public void executeService() {
        try{
            this.requestParamValidation();

            this.service();
        } catch (RequestParamException e) {
            logger.error(e);
            this.apiResult.addProperty("resultCode","405");
        } catch (ServiceException e){
            logger.error(e);
            this.apiResult.addProperty("resultCode","501");
        }

    }

    public JsonObject getApiResult() {
        return this.apiResult;
    }
}
