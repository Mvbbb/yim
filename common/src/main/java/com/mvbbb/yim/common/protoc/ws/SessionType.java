package com.mvbbb.yim.common.protoc.ws;

import java.io.Serializable;

public enum SessionType implements Serializable {
    GROUP, SINGLE;

    public static int getInt(SessionType sessionType) {
        switch (sessionType) {
            case SINGLE:
                return 0;
            case GROUP:
                return 1;
            default:
                break;
        }
        return -1;
    }

    public static SessionType getType(int type) {
        switch (type) {
            case 0:
                return SINGLE;
            case 1:
                return GROUP;
            default:
                break;
        }
        return SINGLE;
    }
}
