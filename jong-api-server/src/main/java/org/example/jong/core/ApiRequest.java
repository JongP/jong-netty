package org.example.jong.core;

import com.google.gson.JsonObject;

public interface ApiRequest {
    void requestParamValidation() throws RequestParamException;

    void service() throws ServiceException;

    void executeService();

    JsonObject getApiResult();
}
