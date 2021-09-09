package com.mvbbb.yim.ws;

import com.mvbbb.yim.common.constant.ProtocConstant;
import com.mvbbb.yim.common.protoc.Ack;
import com.mvbbb.yim.common.protoc.MsgData;
import com.mvbbb.yim.common.protoc.Protobuf;
import com.mvbbb.yim.common.util.BeanConvertor;

public class ProtobufDataPacketUtil {

    public static Protobuf.DataPacket buildMsgData(MsgData msgData){
        Protobuf.DataPacket.Builder dataPacketBuilder = genDataPacketBuilder();
        Protobuf.MsgVO msgVO = BeanConvertor.protoFromMsgData(msgData);
        dataPacketBuilder.setMsgVO(msgVO);
        return dataPacketBuilder.build();
    }

    public static Protobuf.DataPacket buildAck(Ack ack){
        Protobuf.Ack.Builder ackBuilder = Protobuf.Ack.newBuilder();
        ackBuilder.setClientMsgId(ack.getClientMsgId());
        ackBuilder.setServerMsgId(ack.getServerMsgId());
        ackBuilder.setMsg(ack.getMsg());
        Protobuf.Ack ack1 = ackBuilder.build();
        Protobuf.DataPacket.Builder builder = genDataPacketBuilder().setAck(ack1);
        return builder.build();
    }

    private static Protobuf.DataPacket.Builder genDataPacketBuilder(){
        Protobuf.DataPacket.Builder dataPacketBuilder = Protobuf.DataPacket.newBuilder();
        dataPacketBuilder.setHeadFlat(ProtocConstant.HEAD);
        dataPacketBuilder.setVersion(ProtocConstant.VERSION);
        dataPacketBuilder.setCmd(Protobuf.CmdType.MSG_DATA);
        dataPacketBuilder.setMsgType(Protobuf.MsgType.TEXT);
        dataPacketBuilder.setSequenceId(1);
        dataPacketBuilder.setLogId(1);
        return dataPacketBuilder;
    }
}
