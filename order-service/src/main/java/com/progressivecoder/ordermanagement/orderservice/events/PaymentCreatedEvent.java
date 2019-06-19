package com.progressivecoder.ordermanagement.orderservice.events;

public class PaymentCreatedEvent {

    public final String paymentId;

    public final String orderId;

    public final String item;

    public final String ammount;

    public final String rejected;

    public final String rollback;


    public PaymentCreatedEvent(String paymentId, String orderId, String item, String ammount, String rejected, String rollback) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
        this.ammount = ammount;
        this.rejected = rejected;
        this.rollback = rollback;
    }
}
