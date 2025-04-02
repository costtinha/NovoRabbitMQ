package com.example.order_service.service;

import com.example.order_service.cache.OrderCacheRepository;
import com.example.order_service.dtos.OrderDto;
import com.example.order_service.dtos.OrderResponseDTO;
import com.example.order_service.entity.*;
import com.example.order_service.mapper.OrderMapper;
import com.example.order_service.persistance.OrderRepository;
import com.example.order_service.producer.OrderProducer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class OrderService {
    private final OrderRepository repository;
    private final OrderCacheRepository cacheRepository;
    private final OrderMapper mapper;
    private final OrderProducer rabbitProducer;

    public OrderService(OrderRepository repository, OrderCacheRepository cacheRepository, OrderMapper mapper, OrderProducer rabbitProducer) {
        this.repository = repository;
        this.cacheRepository = cacheRepository;
        this.mapper = mapper;
        this.rabbitProducer = rabbitProducer;
    }



    public List<OrderResponseDTO> findOrders() {
        return repository.findAll()
                .stream()
                .map(mapper::toOrderResponseDto)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO saveOrder(OrderDto dto) {
        //OrderCache cache = cacheRepository.save(mapper.toOrderCache(repository.save(mapper.toOrder(dto))));
        Order db = repository.save(mapper.toOrder(dto));
        cacheRepository.save(mapper.toOrderCache(db));
        String message = "Ordem criada, orderId:" + db.getOrderId();
        System.out.println("Mensagem sendo enviada para o rabbit " +message);
        rabbitProducer.sendMessage(mapper.toOrderResponseDto(db));
        return mapper.toOrderResponseDto(db);
        //return mapper.toOrderResponseDto(repository.save(mapper.toOrder(dto)));
    }


    public OrderResponseDTO findOrderById(int id) {
        OrderCache cache = cacheRepository.findById(id).orElse(null);
        if (cache != null){
            System.out.println("Encontrado no cache");
            return mapper.cacheToOrderResponseDto(cache);
        }
        Order db = repository.findById(id).orElse(null);
        if(db != null){
            cacheRepository.save(mapper.toOrderCache(db));
            System.out.println("Salvo no cache");
            return mapper.toOrderResponseDto(db);
        }
        return null;
    }


    public void deleteOrderById(int id) {
        repository.deleteById(id);
        cacheRepository.deleteById(id);
    }
}
