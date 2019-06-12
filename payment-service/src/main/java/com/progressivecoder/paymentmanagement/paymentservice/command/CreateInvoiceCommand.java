package com.progressivecoder.paymentmanagement.paymentservice.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class CreateInvoiceCommand {

    @TargetAggregateIdentifier
    public final String paymentId;

    public final String orderId;

    public final String ammount;

    public CreateInvoiceCommand(String paymentId, String orderId, String ammount ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.ammount = ammount;
    }
}
