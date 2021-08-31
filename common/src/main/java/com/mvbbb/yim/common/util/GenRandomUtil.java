package com.mvbbb.yim.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class GenRandomUtil {
    private static final AtomicLong msgIdAdder = new AtomicLong();
    public static long genMsgId(){
        return msgIdAdder.addAndGet(1);
    }


    private static final AtomicLong userIdAdder = new AtomicLong();
    static{
        userIdAdder.addAndGet(new Random().nextInt(100));
    }
    public static String genUserid() {
        long userid = userIdAdder.addAndGet(1);
        return String.valueOf(userid);
    }

    private static final AtomicLong groupIdAdder = new AtomicLong();
    static {
        groupIdAdder.addAndGet(new Random().nextInt(100));
    }
    public static String genGroupid(){
        long groupid = groupIdAdder.addAndGet(1);
        return String.valueOf(groupid);
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
