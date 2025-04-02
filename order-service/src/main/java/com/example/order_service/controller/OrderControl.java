package com.example.order_service.controller;

import com.example.order_service.dtos.OrderDto;
import com.example.order_service.dtos.OrderResponseDTO;
import com.example.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderControl {
    private final OrderService service;

    @Autowired
    public OrderControl(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderResponseDTO> findOrders(){
        return service.findOrders();
    }

    @PostMapping
    public OrderResponseDTO saveOrder(
            @RequestBody OrderDto dto
    ){
        return service.saveOrder(dto);
    }

    @GetMapping("/{order-id}")
    public OrderResponseDTO findOrderById(
            @PathVariable("order-id") int id
    ){
        return service.findOrderById(id);
    }

    @DeleteMapping("/{order-id}")
    public void deleteOrderById(
            @PathVariable("order-id") int id
    ){
        service.deleteOrderById(id);
    }
}
