package org.example.jong.core;

import org.example.jong.service.RequestParamException;
import org.example.jong.service.ServiceException;

import com.google.gson.JsonObject;

public interface ApiRequest {
    void requestParamValidation() throws RequestParamException;

    void service() throws ServiceException;

    void executeService();

    JsonObject getApiResult();
}
