package com.progressivecoder.paymentmanagement.paymentservice.events;

public class InvoiceCreatedEvent {

    public final String paymentId;

    public final String orderId;

    public final  String ammount;

    public InvoiceCreatedEvent(String paymentId, String orderId, String ammount ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.ammount = ammount;
    }
}
