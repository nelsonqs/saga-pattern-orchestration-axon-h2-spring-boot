package com.progressivecoder.paymentmanagement.paymentservice.controllers;

import com.progressivecoder.paymentmanagement.paymentservice.commands.InvoiceCreateDTO;
import com.progressivecoder.paymentmanagement.paymentservice.services.InvoiceCommandService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/api/orders")
@Api(value = "Order Commands", description = "Order Commands Related Endpoints", tags = "Order Commands")
public class InvoiceCommandController {

    private InvoiceCommandService invoiceCommandService;

    public InvoiceCommandController(InvoiceCommandService invoiceCommandService) {
        this.invoiceCommandService = invoiceCommandService;
    }

    @PostMapping
    public CompletableFuture<String> createOrder(@RequestBody InvoiceCreateDTO invoiceCreateDTO){
        return invoiceCommandService.invoiceOrder(invoiceCreateDTO);
    }
}
