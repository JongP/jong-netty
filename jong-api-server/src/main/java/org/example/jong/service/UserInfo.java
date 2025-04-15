package org.example.jong.service;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.example.jong.core.ApiRequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service("users")
@Scope("prototype")
public class UserInfo extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public UserInfo(Map<String,String> requestMap) {
        super(requestMap);
    }

    @Override
    public void requestParamValidation() throws RequestParamException {
        if(StringUtils.isEmpty(reqData.get("email"))) {
            throw new RequestParamException("email is required");
        }
    }


    @Override
    public void service() throws ServiceException {
        Map<String,Object>  result = sqlSession.selectOne("users.userInfoByEmail", this.reqData);

        if(result!=null){
            String userNo = String.valueOf(result.get("USERNO"));

            this.apiResult.addProperty("resultCode","200");
            this.apiResult.addProperty("message","Success");
            this.apiResult.addProperty("userNo", userNo);
        } else {
            this.apiResult.addProperty("resultCode","404");
            this.apiResult.addProperty("message","Fail");
        }
    }
}
