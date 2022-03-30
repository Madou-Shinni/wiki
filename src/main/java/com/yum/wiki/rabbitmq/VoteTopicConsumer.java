package com.yum.wiki.rabbitmq;

import com.yum.wiki.config.RabbitmqConfig;
import com.yum.wiki.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Yum
 * @version 1.0
 *
 * 点赞消费者
 */
@Service
public class VoteTopicConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(VoteTopicConsumer.class);

    @Autowired
    private WebSocketServer webSocketServer;

    // 监听点赞队列
    @RabbitListener(queues = RabbitmqConfig.QUEUE)
    public void onMessage(String message) {
        LOG.info("RabbitMQ接收到消息：{}",message);
        webSocketServer.sendInfo(message);
    }
}
