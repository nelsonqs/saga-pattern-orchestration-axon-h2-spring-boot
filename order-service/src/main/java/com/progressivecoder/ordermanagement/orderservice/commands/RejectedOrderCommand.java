package com.progressivecoder.ordermanagement.orderservice.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

public class RejectedOrderCommand {

    @TargetAggregateIdentifier
    public final String orderId;

    public final String itemType;

    public final BigDecimal price;

    public final String currency;

    public final String orderStatus;

    public RejectedOrderCommand(String orderId, String itemType, BigDecimal price, String currency, String orderStatus) {
        this.orderId = orderId;
        this.itemType = itemType;
        this.price = price;
        this.currency = currency;
        this.orderStatus = orderStatus;
    }
}
