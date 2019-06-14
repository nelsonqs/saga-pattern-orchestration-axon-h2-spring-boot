package com.progressivecoder.ordermanagement.orderservice.events;

public class RollbackPaymentEvent {

    public final String paymentId;

    public final String orderId;

    public final String item;

    public final String ammount;


    public RollbackPaymentEvent(String paymentId, String orderId, String item, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.item = item;
        this.ammount = ammount;
    }
}
