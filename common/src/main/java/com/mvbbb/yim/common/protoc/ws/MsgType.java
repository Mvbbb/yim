package com.mvbbb.yim.common.protoc.ws;

import java.io.Serializable;

public enum MsgType implements Serializable {
    TEXT, FILE;

    public static int getInt(MsgType msgType) {
        switch (msgType) {
            case TEXT:
                return 0;
            case FILE:
                return 1;
        }
        return -1;
    }

    public static MsgType getType(int type) {
        switch (type) {
            case 0:
                return TEXT;
            case 1:
                return FILE;
        }
        return null;
    }
}
