package com.mvbbb.yim.common.util;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class GenRandomUtil {
    private static final AtomicLong msgIdAdder = new AtomicLong();
    public static long genMsgId(){
        return msgIdAdder.addAndGet(1);
    }

    public static String genUserid() {
        int id = RandomUtil.randomInt(100000, 999999);
        return String.valueOf(id);
    }

    public static String genGroupid(){
        int id = RandomUtil.randomInt(100000, 999999);
        return String.valueOf(id);
    }

    private static List<String> avatars = new ArrayList<>();
    private static Random random = new Random();
    static{
        avatars.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(1).png");
        avatars.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(2).png");
        avatars.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(3).png");
        avatars.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(4).png");
        avatars.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(5).png");
        avatars.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(6).png");
        avatars.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(7).png");
    }
    public static String randomAvatar() {
        return avatars.get(random.nextInt(avatars.size()));
    }

    public static String getSalt() {
        return "1";
    }
}
