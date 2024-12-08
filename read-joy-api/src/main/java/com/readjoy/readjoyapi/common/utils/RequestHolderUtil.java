package com.readjoy.readjoyapi.common.utils;

/**
 * Description: 请求上下文
 */
public class RequestHolderUtil {

    private static final ThreadLocal<UserTokenUtil> threadLocal = new ThreadLocal<>();

    public static void set(UserTokenUtil requestInfo) {
        threadLocal.set(requestInfo);
    }

    public static UserTokenUtil get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }


}
