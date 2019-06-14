package com.progressivecoder.ordermanagement.orderservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RejectedPaymentCommand {

    @TargetAggregateIdentifier
    public final String paymentId;
    public final String orderId;
    public final String item;
    public final String ammount;


    public RejectedPaymentCommand(String paymentId, String orderId, String item, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
        this.ammount = ammount;
    }
}
