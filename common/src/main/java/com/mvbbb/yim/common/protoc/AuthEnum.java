package com.mvbbb.yim.common.protoc;

import java.io.Serializable;

public enum AuthEnum implements Serializable {
    WRONG_PASSWORD, USER_NOT_EXIST, WRONG_TOKEN, SUCCESS;
}
