package com.progressivecoder.ordermanagement.orderservice.controllers;

import com.progressivecoder.ordermanagement.orderservice.commands.OrderCreateDTO;
import com.progressivecoder.ordermanagement.orderservice.services.commands.OrderCommandService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "api")
@Api(value = "Order Commands", description = "Order Commands Related Endpoints", tags = "Order Commands")
public class OrderCommandController {

    private OrderCommandService orderCommandService;

    public OrderCommandController(OrderCommandService orderCommandService) {
        this.orderCommandService = orderCommandService;
    }

    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public CompletableFuture<String> createOrder(@RequestBody OrderCreateDTO orderCreateDTO){
        return orderCommandService.createOrder(orderCreateDTO);
    }

    @RequestMapping(value = "/orders-rollback", method = RequestMethod.POST)
    public CompletableFuture<String> rollbackOrder(@RequestBody OrderCreateDTO orderCreateDTO){
        return orderCommandService.rollbackOrder(orderCreateDTO);
    }

    @RequestMapping(value = "/orders-rejected", method = RequestMethod.POST)
    public CompletableFuture<String> rejectedOrder(@RequestBody OrderCreateDTO orderCreateDTO){
        return orderCommandService.rollbackOrder(orderCreateDTO);
    }
}
