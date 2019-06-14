package com.progressivecoder.shippingmanagement.shippingservice.services;

import com.progressivecoder.shippingmanagement.shippingservice.command.CreateShippingCommand;
import com.progressivecoder.shippingmanagement.shippingservice.command.RollbackShippingCommand;
import com.progressivecoder.shippingmanagement.shippingservice.dto.ShippingCreateDTO;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ShippingCommandServiceImpl implements ShippingCommandService {

    private final CommandGateway commandGateway;

    public ShippingCommandServiceImpl(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @Override
    public CompletableFuture<String> shippingOrder(ShippingCreateDTO invoiceCreateDTO) {
        return commandGateway.send(new CreateShippingCommand(invoiceCreateDTO.getShippingId(), invoiceCreateDTO.getOrderId(), invoiceCreateDTO.getPaymentId(), invoiceCreateDTO.getItem()));
    }

    @Override
    public CompletableFuture<String> shippingRollbackOrder(ShippingCreateDTO invoiceCreateDTO) {
        return commandGateway.send(new RollbackShippingCommand(invoiceCreateDTO.getShippingId(), invoiceCreateDTO.getOrderId(), invoiceCreateDTO.getPaymentId(), invoiceCreateDTO.getItem()));
    }

    @Override
    public CompletableFuture<String> shippingRejectedOrder(ShippingCreateDTO invoiceCreateDTO) {
        return commandGateway.send(new RollbackShippingCommand(invoiceCreateDTO.getShippingId(), invoiceCreateDTO.getOrderId(), invoiceCreateDTO.getPaymentId(), invoiceCreateDTO.getItem()));
    }
}
