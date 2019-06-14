package com.progressivecoder.ordermanagement.orderservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RejectedPaymentCommand {

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;
    public final String item;

    public RejectedPaymentCommand(String paymentId, String orderId, String item) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
    }
}
