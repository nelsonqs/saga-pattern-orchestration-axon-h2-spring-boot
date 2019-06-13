package com.progressivecoder.paymentmanagement.paymentservice.services;

import com.progressivecoder.paymentmanagement.paymentservice.command.CreateInvoiceCommand;
import com.progressivecoder.paymentmanagement.paymentservice.command.RejectedInvoiceCommand;
import com.progressivecoder.paymentmanagement.paymentservice.commands.InvoiceCreateDTO;
import com.progressivecoder.paymentmanagement.paymentservice.commands.RejectedInvoiceDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class InvoiceCommandServiceImpl implements InvoiceCommandService {

    private final CommandGateway commandGateway;

    public InvoiceCommandServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> invoiceOrder(InvoiceCreateDTO invoiceCreateDTO) {
        return commandGateway.send(new CreateInvoiceCommand(invoiceCreateDTO.getPaymentId(), invoiceCreateDTO.getOrderId(), invoiceCreateDTO.getAmmount()));
    }

    @Override
    public CompletableFuture<String> rejectedOrder(RejectedInvoiceDTO invoiceCreateDTO) {
        return commandGateway.send(new RejectedInvoiceCommand(invoiceCreateDTO.getPaymentId(), invoiceCreateDTO.getOrderId(), invoiceCreateDTO.getAmmount()));
    }
}
