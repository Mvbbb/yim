package com;


import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.CmdIdEnum;
import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import com.mvbbb.yim.common.protoc.ws.request.ByeRequest;
import org.junit.Test;

public class test {

    @Test
    public void test1(){
        DataPacket<GreetRequest> authRequestDataPacket = new DataPacket<>();
        GreetRequest authRequest = new GreetRequest();
        authRequest.setToken("token");
        authRequest.setUserId("1");
        authRequestDataPacket.setCmdId(CmdIdEnum.GREET_REQ);
        authRequestDataPacket.setData(authRequest);
        String s = JSONObject.toJSONString(authRequestDataPacket);
        System.out.println(s);
    }

    @Test
    public void test2(){

        DataPacket<ByeRequest> byeRequestDataPacket = new DataPacket<>();
        ByeRequest byeRequest = new ByeRequest();
        byeRequest.setToken("token");
        byeRequest.setUserId("1");
        byeRequestDataPacket.setData(byeRequest);
        byeRequestDataPacket.setCmdId(CmdIdEnum.BYE_REQ);
        String s = JSONObject.toJSONString(byeRequestDataPacket);
        System.out.println(s);
    }

    // 私聊消息
    @Test
    public void test3(){
        DataPacket<MsgData> msgDataDataPacket = new DataPacket<>();
        MsgData msgData = new MsgData();
        msgData.setData("你好");
        msgData.setMsgType(MsgType.TEXT);
        msgData.setSessionType(SessionType.SINGLE);
        msgData.setClientMsgId("1");
        msgData.setToSessionId("2");
        msgData.setFromUserId("1");
        msgDataDataPacket.setData(msgData);
        msgDataDataPacket.setCmdId(CmdIdEnum.MSG_DATA);
        String s = JSONObject.toJSONString(msgDataDataPacket);
        System.out.println(s);
    }

    // 群聊消息
    @Test
    public void test4(){
        DataPacket<MsgData> msgDataDataPacket = new DataPacket<>();
        MsgData msgData = new MsgData();
        msgData.setData("群聊消息测试");
        msgData.setMsgType(MsgType.TEXT);
        msgData.setSessionType(SessionType.GROUP);
        msgData.setClientMsgId("1");
        msgData.setToSessionId("1");
        msgData.setFromUserId("1");
        msgDataDataPacket.setData(msgData);
        msgDataDataPacket.setCmdId(CmdIdEnum.MSG_DATA);
        String s = JSONObject.toJSONString(msgDataDataPacket);
        System.out.println(s);
    }
}
