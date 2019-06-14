package com.progressivecoder.ordermanagement.orderservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RollbackShippingCommand {

    @TargetAggregateIdentifier
    public final String shippingId;
    public final String orderId;
    public final String paymentId;
    public final String itemType;

    public RollbackShippingCommand(String shippingId, String orderId, String paymentId, String itemType) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.itemType = itemType;
    }
}
