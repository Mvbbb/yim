package com.mvbbb.yim.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsServerRoute implements Serializable {
    private String ip;
    private int port;
}
