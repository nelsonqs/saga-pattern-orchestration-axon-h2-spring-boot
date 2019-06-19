package com.progressivecoder.ordermanagement.orderservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateShippingCommand {

    @TargetAggregateIdentifier
    public final String shippingId;
    public final String orderId;
    public final String paymentId;
    public final String itemType;
    public final String ammount;
    public final String rejected;
    public final String rollback;

    public CreateShippingCommand(String shippingId, String orderId, String paymentId, String itemType, String ammount, String rejected, String rollback) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.itemType = itemType;
        this.ammount = ammount;
        this.rejected = rejected;
        this.rollback = rollback;
    }
}
