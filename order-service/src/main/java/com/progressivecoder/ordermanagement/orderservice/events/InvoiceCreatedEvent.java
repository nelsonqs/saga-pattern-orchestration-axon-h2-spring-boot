package com.progressivecoder.ordermanagement.orderservice.events;

public class InvoiceCreatedEvent  {

    public final String paymentId;

    public final String orderId;


    public InvoiceCreatedEvent(String paymentId, String orderId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
    }
}
