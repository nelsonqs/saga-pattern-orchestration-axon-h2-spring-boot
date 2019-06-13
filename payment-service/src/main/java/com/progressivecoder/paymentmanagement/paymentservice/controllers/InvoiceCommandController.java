package com.progressivecoder.paymentmanagement.paymentservice.controllers;

import com.progressivecoder.paymentmanagement.paymentservice.commands.InvoiceCreateDTO;
import com.progressivecoder.paymentmanagement.paymentservice.commands.RejectedInvoiceDTO;
import com.progressivecoder.paymentmanagement.paymentservice.services.InvoiceCommandService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "api")


@Api(value = "Payment Commands", description = "Payment Commands Related Endpoints", tags = "Payment Commands")
public class InvoiceCommandController {

    private InvoiceCommandService invoiceCommandService;

    public InvoiceCommandController(InvoiceCommandService invoiceCommandService) {
        this.invoiceCommandService = invoiceCommandService;
    }

    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public CompletableFuture<String> createOrder(@RequestBody InvoiceCreateDTO invoiceCreateDTO){
        return invoiceCommandService.invoiceOrder(invoiceCreateDTO);
    }

    @RequestMapping(value = "/payment-rejected", method = RequestMethod.POST)
    public CompletableFuture<String> rejectedOrder(@RequestBody RejectedInvoiceDTO invoiceCreateDTO) {
        return invoiceCommandService.rejectedOrder(invoiceCreateDTO);
    }
}
