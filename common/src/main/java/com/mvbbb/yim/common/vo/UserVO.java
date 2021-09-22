package com.mvbbb.yim.common.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class UserVO implements Serializable {
    private String userId;
    private String username;
    private String avatar;
    private Boolean online;
}
