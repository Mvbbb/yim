package com.mvbbb.yim.common.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {
    private String userId;
    private String username;
    private String avatar;
}
