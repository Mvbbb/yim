package com.mvbbb.yim.common.protoc.ws;


public enum CmdType {
    GREET, BYE, MSG_DATA, ACK;

    public static byte getByte(CmdType msgData) {
        switch (msgData){
            case GREET:
                return 1;
            case BYE:
                return 2;
            case MSG_DATA:
                return 3;
            case ACK:
                return 4;
            default:
                return 0;
        }
    }
}