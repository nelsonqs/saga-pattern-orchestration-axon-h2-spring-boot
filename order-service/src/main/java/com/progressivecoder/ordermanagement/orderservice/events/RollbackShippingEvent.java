package com.progressivecoder.ordermanagement.orderservice.events;

public class RollbackShippingEvent {

    public final String shippingId;

    public final String orderId;

    public final String paymentId;

    public final String itemType;


    public RollbackShippingEvent(String shippingId, String orderId, String paymentId, String itemType) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.itemType = itemType;
    }
}
