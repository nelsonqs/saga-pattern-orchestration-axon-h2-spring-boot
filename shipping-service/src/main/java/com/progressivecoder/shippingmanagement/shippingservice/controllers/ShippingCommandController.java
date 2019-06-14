package com.progressivecoder.shippingmanagement.shippingservice.controllers;

import com.progressivecoder.shippingmanagement.shippingservice.dto.ShippingCreateDTO;
import com.progressivecoder.shippingmanagement.shippingservice.services.ShippingCommandService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "api")
@Api(value = "Shipping Commands", description = "shipping Commands Related Endpoints", tags = "Shipping Commands")
public class ShippingCommandController {

    private ShippingCommandService shippingCommandService;

    public ShippingCommandController(ShippingCommandService shippingCommandService) {
        this.shippingCommandService = shippingCommandService;
    }

    @RequestMapping(value = "/shipping", method = RequestMethod.POST)
    public CompletableFuture<String> createShipping(@RequestBody ShippingCreateDTO  shippingCreateDTO){
        return shippingCommandService.shippingOrder(shippingCreateDTO);
    }

    @RequestMapping(value = "/shipping-rollback", method = RequestMethod.POST)
    public CompletableFuture<String> createShippingRollback(@RequestBody ShippingCreateDTO  shippingCreateDTO){
        return shippingCommandService.shippingRollbackOrder(shippingCreateDTO);
    }

    @RequestMapping(value = "/shipping-rejected", method = RequestMethod.POST)
    public CompletableFuture<String> createShippingRejected(@RequestBody ShippingCreateDTO  shippingCreateDTO){
        return shippingCommandService.shippingRollbackOrder(shippingCreateDTO);
    }
}
