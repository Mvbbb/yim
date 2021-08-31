package com;


import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.request.AuthRequest;
import org.junit.Test;

public class test {

    @Test
    public void test1(){
        DataPacket<AuthRequest> authRequestDataPacket = new DataPacket<>();
        AuthRequest authRequest = new AuthRequest();
        authRequest.setToken("token");
        authRequest.setUserId("1");
        authRequestDataPacket.setCmdId(2);
        authRequestDataPacket.setData(authRequest);
        String s = JSONObject.toJSONString(authRequestDataPacket);
        System.out.println(s);
    }
}
