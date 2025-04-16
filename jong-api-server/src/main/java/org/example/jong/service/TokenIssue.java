package org.example.jong.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.example.jong.core.ApiRequestTemplate;
import org.example.jong.core.JedisHelper;
import org.example.jong.core.KeyMaker;
import org.example.jong.service.dao.TokenKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

@Service("tokenIssue")
@Scope("prototype")
public class TokenIssue extends ApiRequestTemplate {
    private final static JedisHelper helper = JedisHelper.getInstance();

    @Autowired
    SqlSession sqlSession;

    public TokenIssue(Map<String,String> requestMap) {
        super(requestMap);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {
        if(StringUtils.isEmpty(reqData.get("userNo"))) {
            throw new RequestParamException("userId is required");
        }

        if(StringUtils.isEmpty(reqData.get("password"))) {
            throw new RequestParamException("password is required");
        }

    }

    @Override
    public void service() throws ServiceException {
        Jedis jedis = null;

        try{
            Map<String,Object> result = sqlSession.selectOne("users.userInfoByPassword",reqData);

            if(result!=null){
                final long threeHour = 3 * 60 * 60;
                long issueDate = System.currentTimeMillis()/1000;
                String email = String.valueOf(result.get("USERID"));

                JsonObject token = new JsonObject();
                token.addProperty("issueDate", issueDate);
                token.addProperty("expireDate", issueDate + threeHour);
                token.addProperty("email",email);
                token.addProperty("userNo", reqData.get("userNo"));

                //store token
                KeyMaker tokenKey = new TokenKey(email, issueDate);
                jedis = helper.getConnection();
                jedis.setex(tokenKey.getKey(), (int) threeHour, token.toString());

                //helper
                this.apiResult.addProperty("resultCode","200");
                this.apiResult.addProperty("messaage","Success");
                this.apiResult.addProperty("token", tokenKey.getKey());
            }
            else{
                this.apiResult.addProperty("resultCode","400");
                this.apiResult.addProperty("message","Fail");
            }

            helper.returnResource(jedis);
        } catch (Exception e){
            helper.returnResource(jedis);
        }
    }
}
