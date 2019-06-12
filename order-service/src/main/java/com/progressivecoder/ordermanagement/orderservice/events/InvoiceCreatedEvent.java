package com.progressivecoder.ordermanagement.orderservice.events;

public class InvoiceCreatedEvent  {

    public final String paymentId;

    public final String orderId;

    public final String item;


    public InvoiceCreatedEvent(String paymentId, String orderId, String item) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
    }
}
