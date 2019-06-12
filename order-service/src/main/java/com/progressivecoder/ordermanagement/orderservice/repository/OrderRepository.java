package com.progressivecoder.ordermanagement.orderservice.repository;

import com.progressivecoder.ordermanagement.orderservice.OrderCreateEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderCreateEntity, String> {

}
