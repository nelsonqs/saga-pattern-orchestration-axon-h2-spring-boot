package com.progressivecoder.ordermanagement.orderservice.services.commands;

import com.progressivecoder.ordermanagement.orderservice.commands.OrderCreateDTO;

import java.util.concurrent.CompletableFuture;

public interface OrderCommandService {

    public CompletableFuture<String> createOrder(OrderCreateDTO orderCreateDTO);

}
