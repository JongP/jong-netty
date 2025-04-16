package org.example.jong.core;

import org.example.jong.service.exception.RequestParamException;
import org.example.jong.service.exception.ServiceException;

import com.google.gson.JsonObject;

public interface ApiRequest {
    void requestParamValidation() throws RequestParamException;

    void service() throws ServiceException;

    void executeService();

    JsonObject getApiResult();
}
