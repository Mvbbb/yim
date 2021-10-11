package com.mvbbb.yim.common.util;

import com.mvbbb.yim.common.constant.MqConstant;

/**
 * @author: mvbbb
 */
public class MqUtil {

    public static String getWsTopicName(String host, int port) {
        return MqConstant.TOPIC_DELIVER_WS + "_" + host.replace(".", "_") + "_" + port;
    }

    public static String getWsConsumerGroupName(String host, int port) {
        return MqConstant.GROUP_CONSUMER_WS + "_" + host.replace(".", "_") + "_" + port;
    }
}
