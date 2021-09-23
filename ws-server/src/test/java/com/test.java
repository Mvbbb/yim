package com;

import com.alibaba.fastjson.JSONObject;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.Bye;
import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.ws.CmdType;
import com.mvbbb.yim.common.protoc.ws.MsgType;
import com.mvbbb.yim.common.protoc.ws.SessionType;
import com.mvbbb.yim.common.protoc.ws.request.GreetRequest;
import org.junit.Test;


public class test {

    @Test
    public void test1() {
        DataPacket<MsgData> dataPacket = new DataPacket<>();

        MsgData msgData = new MsgData();
        msgData.setClientMsgId("1");
        msgData.setServerMsgId(1);
        msgData.setFromUserId("1");
        msgData.setSessionType(SessionType.SINGLE);
        msgData.setToUserId("2");
        msgData.setMsgType(MsgType.TEXT);
        msgData.setData("你好1q");

        dataPacket.setVersion(1);
        dataPacket.setCmdType(CmdType.MSG_DATA);
        dataPacket.setLogId(1);
        dataPacket.setSequenceId(1);
        dataPacket.setData(msgData);

        String json = JSONObject.toJSONString(dataPacket);
        System.out.println(json);
    }

    @Test
    public void test2() {
        DataPacket<GreetRequest> dataPacket = new DataPacket<>();

        GreetRequest greetRequest = new GreetRequest();
        greetRequest.setUserId("1");
        greetRequest.setToken("2");

        dataPacket.setCmdType(CmdType.GREET);
        dataPacket.setVersion(1);
        dataPacket.setLogId(1);
        dataPacket.setSequenceId(1);
        dataPacket.setData(greetRequest);

        String json = JSONObject.toJSONString(dataPacket);
        System.out.println(json);
    }

    @Test
    public void test3() {
        DataPacket<Ack> dataPacket = new DataPacket<>();

        Ack ack = new Ack();
        ack.setClientMsgId("1");
        ack.setServerMsgId(111);
        ack.setUserId("1");
        ack.setMsg("成功接收消息");

        dataPacket.setVersion(1);
        dataPacket.setSequenceId(1);
        dataPacket.setCmdType(CmdType.ACK);
        dataPacket.setLogId(1);
        dataPacket.setData(ack);

        String json = JSONObject.toJSONString(dataPacket);
        System.out.println(json);
    }

    @Test
    public void test4() {
        DataPacket<Bye> dataPacket = new DataPacket<>();

        Bye bye = new Bye();

        dataPacket.setVersion(1);
        dataPacket.setSequenceId(1);
        dataPacket.setCmdType(CmdType.BYE);
        dataPacket.setLogId(1);
        dataPacket.setData(bye);

        String json = JSONObject.toJSONString(dataPacket);
        System.out.println(json);
    }

    @Test
    public void test5() {
        DataPacket<MsgData> dataPacket = new DataPacket<>();

        MsgData msgData = new MsgData();
        msgData.setClientMsgId("1");
        msgData.setServerMsgId(1);
        msgData.setFromUserId("1");
        msgData.setSessionType(SessionType.GROUP);
        msgData.setGroupId("1");
        msgData.setMsgType(MsgType.TEXT);
        msgData.setData("这是一条群聊消息");

        dataPacket.setVersion(1);
        dataPacket.setCmdType(CmdType.MSG_DATA);
        dataPacket.setLogId(1);
        dataPacket.setSequenceId(1);
        dataPacket.setData(msgData);

        String json = JSONObject.toJSONString(dataPacket);
        System.out.println(json);
    }

    @Test
    public void test10() {
        String json = "{\"cmdType\":\"ACK\",\"data\":{\"clientMsgId\":\"1\",\"msg\":\"成功接收消息\",\"serverMsgId\":1,\"userId\":\"1\"},\"headFlag\":55,\"logId\":1,\"sequenceId\":1,\"version\":1}";
        DataPacket dataPacket = JSONObject.parseObject(json, DataPacket.class);
        System.out.println(dataPacket.getData().getClass());
        System.out.println(dataPacket);
    }
}