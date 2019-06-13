package com.progressivecoder.paymentmanagement.paymentservice.events;

public class RejectedInvoiceEvent {

    public final String paymentId;

    public final String orderId;

    public final String ammount;

    public RejectedInvoiceEvent(String paymentId, String orderId, String ammount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.ammount = ammount;
    }
}
