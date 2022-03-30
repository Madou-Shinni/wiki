//package com.yum.wiki.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author Yum
// * @version 1.0
// */
//@Configuration
//public class RabbitmqConfig {
//
//    // 点赞队列
//    public static final String QUEUE = "VOTE_QUEUE";
//    // 点赞交换机
//    public static final String EXCHANGE = "VOTE_TOPIC";
//
//    // 创建消息队列
//    @Bean
//    public Queue queue() {
//        return new Queue(QUEUE);
//    }
//    // 创建交换机
//    @Bean
//    public TopicExchange topicExchange() {
//        return new TopicExchange(EXCHANGE);
//    }
//    // 绑定交换机
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(topicExchange()).with("VOTE.#");
//    }
//}
