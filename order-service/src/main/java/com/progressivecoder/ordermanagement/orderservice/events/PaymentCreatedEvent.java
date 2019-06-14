package com.progressivecoder.ordermanagement.orderservice.events;

public class PaymentCreatedEvent {

    public final String paymentId;

    public final String orderId;

    public final String item;

    public final String ammount;


    public PaymentCreatedEvent(String paymentId, String orderId, String item, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
        this.ammount = ammount;
    }
}
