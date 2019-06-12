package com.progressivecoder.ordermanagement.orderservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateInvoiceCommand {

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;
    public final String item;

    public CreateInvoiceCommand(String paymentId, String orderId, String item) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
    }
}
