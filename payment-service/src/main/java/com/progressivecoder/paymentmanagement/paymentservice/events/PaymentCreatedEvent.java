package com.progressivecoder.paymentmanagement.paymentservice.events;

public class PaymentCreatedEvent {

    public final String paymentId;

    public final String orderId;

    public final  String ammount;

    public PaymentCreatedEvent(String paymentId, String orderId, String ammount ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.ammount = ammount;
    }
}
