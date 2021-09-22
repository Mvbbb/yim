package com.mvbbb.yim.common.util;

import cn.hutool.core.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public final class GenRandomUtil {
    private static final AtomicLong MSG_ID_ADDER = new AtomicLong();
    private static final List<String> AVATARS = new ArrayList<>();
    private static final Random RANDOM = new Random();

    static {
        AVATARS.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(1).png");
        AVATARS.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(2).png");
        AVATARS.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(3).png");
        AVATARS.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(4).png");
        AVATARS.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(5).png");
        AVATARS.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(6).png");
        AVATARS.add("https://gitee.com/mvbbb/filehost/raw/master/avatar/avatar%20(7).png");
    }

    public static String genUserid() {
        int id = RandomUtil.randomInt(100000, 999999);
        return String.valueOf(id);
    }

    public static String genGroupid() {
        int id = RandomUtil.randomInt(100000, 999999);
        return String.valueOf(id);
    }

    public static String randomAvatar() {
        return AVATARS.get(RANDOM.nextInt(AVATARS.size()));
    }

}
