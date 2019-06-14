package com.progressivecoder.ordermanagement.orderservice.events;

public class PaymentCreatedEvent {

    public final String paymentId;

    public final String orderId;

    public final String item;


    public PaymentCreatedEvent(String paymentId, String orderId, String item) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
    }
}
