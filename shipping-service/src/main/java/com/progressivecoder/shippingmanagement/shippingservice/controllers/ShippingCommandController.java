package com.progressivecoder.shippingmanagement.shippingservice.controllers;

import com.progressivecoder.shippingmanagement.shippingservice.dto.ShippingCreateDTO;
import com.progressivecoder.shippingmanagement.shippingservice.services.ShippingCommandService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/api/shipping")
@Api(value = "Shipping Commands", description = "shipping Commands Related Endpoints", tags = "Shipping Commands")
public class ShippingCommandController {

    private ShippingCommandService shippingCommandService;

    public ShippingCommandController(ShippingCommandService shippingCommandService) {
        this.shippingCommandService = shippingCommandService;
    }

    @PostMapping
    public CompletableFuture<String> createOrder(@RequestBody ShippingCreateDTO  shippingCreateDTO){
        return shippingCommandService.shippingOrder(shippingCreateDTO);
    }
}
