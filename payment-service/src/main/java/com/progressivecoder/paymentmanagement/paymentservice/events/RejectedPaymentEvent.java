package com.progressivecoder.paymentmanagement.paymentservice.events;

public class RejectedPaymentEvent {

    public final String paymentId;

    public final String orderId;

    public final String ammount;

    public RejectedPaymentEvent(String paymentId, String orderId, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.ammount = ammount;
    }
}
