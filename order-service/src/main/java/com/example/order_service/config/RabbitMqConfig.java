package com.example.order_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE = "exchange";
    public static final String ORDER_QUEUE = "order_queue";
    public static final String ORDER_ROUTING_KEY = "order_key";

    @Bean
    public DirectExchange exchange(){
        return new  DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue queue(){
        return new  Queue(ORDER_QUEUE,true);
    }

    @Bean
    public Binding binding_order_queue(){
       return BindingBuilder.bind(queue()).to(exchange()).with(ORDER_ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }


}
