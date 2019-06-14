package com.progressivecoder.paymentmanagement.paymentservice.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RejectedPaymentCommand {

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;

    public final String ammount;

    public RejectedPaymentCommand(String paymentId, String orderId, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.ammount = ammount;
    }
}
