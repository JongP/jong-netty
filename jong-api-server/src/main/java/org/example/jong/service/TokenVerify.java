package org.example.jong.service;

import java.util.Map;

import org.example.jong.core.ApiRequestTemplate;
import org.example.jong.core.JedisHelper;
import org.example.jong.service.exception.RequestParamException;
import org.example.jong.service.exception.ServiceException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

@Service("tokenVerify")
@Scope("prototype")
public class TokenVerify extends ApiRequestTemplate {
    private static final JedisHelper jedisHelper = JedisHelper.getInstance();

    public TokenVerify(Map<String, String> reqMap) {
        super(reqMap);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {
        if(StringUtils.isEmpty(reqData.get("token"))) {
            throw new RequestParamException("token is required");
        }
    }

    @Override
    public void service() throws ServiceException {
        Jedis jedis = null;
        try{
            jedis = jedisHelper.getConnection();
            String tokenSring = jedis.get(reqData.get("token"));

            if(tokenSring ==null){
                this.apiResult.addProperty("resultCode","404");
                this.apiResult.addProperty("message","Fail");
            } else {
                Gson gson = new Gson();
                JsonObject token = gson.fromJson(tokenSring, JsonObject.class);

                this.apiResult.addProperty("resultCode","200");
                this.apiResult.addProperty("message","Success");
                this.apiResult.add("issueDate", token.get("issueDate"));
                this.apiResult.add("email", token.get("email"));
                this.apiResult.add("userNo",token.get("userNo"));
            }
        } catch (Exception e){
            jedisHelper.returnResource(jedis);
        }
    }
}
