package com.mvbbb.yim.common.util;

import com.mvbbb.yim.common.protoc.DataPacket;
import com.mvbbb.yim.common.protoc.enums.CtrlResponseType;
import com.mvbbb.yim.common.protoc.response.CtrlResponse;

import java.util.Date;

public final class DataPacketBuilder {

    public static <T> DataPacket<CtrlResponse<T>> buildOkCtrlResponse(String clientMsgId, String msg, Date timestamp, T ctrlResponseData){
        DataPacket<CtrlResponse<T>> dataPacket = new DataPacket<CtrlResponse<T>>();
        CtrlResponse<T> ctrlResponse = new CtrlResponse<>();
        ctrlResponse.setData(ctrlResponseData);
        ctrlResponse.setMsg(msg);
        ctrlResponse.setTimestamp(timestamp);
        ctrlResponse.setType(CtrlResponseType.OK);
        ctrlResponse.setClientMsgId(clientMsgId);
        dataPacket.setData(ctrlResponse);
        dataPacket.setVersion(1);
        dataPacket.setCmdId(22);
        return dataPacket;
    }

   public static <T> DataPacket<CtrlResponse<T>> buildErrorCtrlResponse(String clientMsgId, String msg, Date timestamp, T ctrlResponseData){
        DataPacket<CtrlResponse<T>> dataPacket = new DataPacket<CtrlResponse<T>>();
        CtrlResponse<T> ctrlResponse = new CtrlResponse<>();
        ctrlResponse.setData(ctrlResponseData);
        ctrlResponse.setMsg(msg);
        ctrlResponse.setTimestamp(timestamp);
        ctrlResponse.setType(CtrlResponseType.ERR);
        ctrlResponse.setClientMsgId(clientMsgId);
        dataPacket.setData(ctrlResponse);
        dataPacket.setVersion(1);
        dataPacket.setCmdId(22);
        return dataPacket;
    }
}
