package com.progressivecoder.ordermanagement.orderservice.events;

public class RejectedShippingEvent {

    public final String shippingId;

    public final String orderId;

    public final String paymentId;

    public final String itemType;


    public RejectedShippingEvent(String shippingId, String orderId, String paymentId, String itemType) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.itemType = itemType;
    }
}
