package com.example.order_service.mapper;

import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderCache;
import com.example.order_service.dtos.OrderDto;
import com.example.order_service.dtos.OrderResponseDTO;
import org.springframework.stereotype.Component;
@Component
public class OrderMapper {
    public OrderResponseDTO toOrderResponseDto(Order order){
        return new OrderResponseDTO(order.getDescription(),order.getPrice());
    }

    public Order toOrder(OrderDto dto){
        return new Order(dto.description(),dto.price(),dto.confirmationStatus());
    }

    public OrderCache toOrderCache(Order order){
        return new OrderCache(order.getOrderId(),order.getDescription(),order.getPrice(),order.isConfirmationStatus());
    }

    public OrderResponseDTO cacheToOrderResponseDto(OrderCache cache){
        return new OrderResponseDTO(cache.getDescription(),cache.getPrice());
    }
}
