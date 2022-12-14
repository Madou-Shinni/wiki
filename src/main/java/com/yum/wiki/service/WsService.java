package com.yum.wiki.service;

import com.yum.wiki.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Yum
 * @version 1.0
 *
 * 单独创建一个类：可以让异步化生效
 * websocket发送消息
 */
@Service
public class WsService {
    private static final Logger LOG = LoggerFactory.getLogger(WsService.class);

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * websocket推送消息
     *
     * @param message 消息内容
     */
    @Async // 让这个方法异步化
    public void sendInfo(String message,String logId) {
        MDC.put("LOG_ID",logId);
        webSocketServer.sendInfo(message);
    }
}
