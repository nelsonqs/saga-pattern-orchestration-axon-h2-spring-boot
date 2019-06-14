package com.progressivecoder.ordermanagement.orderservice.events;

public class RejectedPaymentEvent {

    public final String paymentId;

    public final String orderId;

    public final String item;


    public RejectedPaymentEvent(String paymentId, String orderId, String item) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
    }
}
