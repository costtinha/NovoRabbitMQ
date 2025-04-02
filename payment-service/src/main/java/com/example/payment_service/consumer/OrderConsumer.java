package com.example.payment_service.consumer;

import com.example.payment_service.config.RabbitMqConfig;
import com.example.payment_service.dtos.OrderResponseDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    @RabbitListener(queues = RabbitMqConfig.ORDER_QUEUE)
    public void receiveMessage(OrderResponseDTO dto){
        System.out.println("Mensagem json recebida: " + dto);

    }
}
