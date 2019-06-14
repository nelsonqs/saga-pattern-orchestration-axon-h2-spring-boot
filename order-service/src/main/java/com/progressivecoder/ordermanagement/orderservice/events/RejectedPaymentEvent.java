package com.progressivecoder.ordermanagement.orderservice.events;

public class RejectedPaymentEvent {

    public final String paymentId;

    public final String orderId;

    public final String item;

    public final String ammount;


    public RejectedPaymentEvent(String paymentId, String orderId, String item, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
        this.ammount = ammount;
    }
}
