package com.example.order_service.producer;

import com.example.order_service.config.RabbitMqConfig;
import com.example.order_service.dtos.OrderResponseDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(OrderResponseDTO dto){
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE,RabbitMqConfig.ORDER_ROUTING_KEY, dto);
        System.out.println("Mensagem enviada por json");
    }
}
