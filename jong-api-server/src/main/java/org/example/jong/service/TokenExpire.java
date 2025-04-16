package org.example.jong.service;

import java.util.Map;

import org.example.jong.core.ApiRequestTemplate;
import org.example.jong.core.JedisHelper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;

@Service("tokenExpire")
@Scope("prototype")
public class TokenExpire extends ApiRequestTemplate {

    private static final JedisHelper helper = JedisHelper.getInstance();

    public TokenExpire(Map<String, String> reqMap) {
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
            jedis = helper.getConnection();
            long result = jedis.del(reqData.get("token"));
            System.out.println(result);

            //herlper
            this.apiResult.addProperty("resultCode","200");
            this.apiResult.addProperty("message","Success");
            this.apiResult.addProperty("token",reqData.get("token"));
        } catch (Exception e){
            helper.returnResource(jedis);
        }
    }
}
