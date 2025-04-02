package com.example.order_service.cache;

import com.example.order_service.entity.OrderCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCacheRepository extends CrudRepository<OrderCache,Integer> {
}
