package com.progressivecoder.ordermanagement.orderservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreatePaymentCommand {

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;
    public final String item;
    public final String ammount;

    public CreatePaymentCommand(String paymentId, String orderId, String item, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
        this.ammount = ammount;
    }
}
