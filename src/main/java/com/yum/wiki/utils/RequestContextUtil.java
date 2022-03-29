package com.yum.wiki.utils;



import java.io.Serializable;

/**
 * @author Yum
 * @version 1.0
 *
 * 请求上下文工具
 */
public class RequestContextUtil implements Serializable {
    private static ThreadLocal<String> remoteAddr = new ThreadLocal<>();

    public static String getRemoteAddr() {
        return remoteAddr.get();
    }

    public static void setRemoteAddr(String remoteAddr) {
        RequestContextUtil.remoteAddr.set(remoteAddr);
    }
}
