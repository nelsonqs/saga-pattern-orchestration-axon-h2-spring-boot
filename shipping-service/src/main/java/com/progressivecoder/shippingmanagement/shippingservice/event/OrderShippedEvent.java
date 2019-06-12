package com.progressivecoder.shippingmanagement.shippingservice.event;

public class OrderShippedEvent {

    public final String shippingId;

    public final String orderId;

    public final String paymentId;

    public final String item;

    public OrderShippedEvent(String shippingId, String orderId, String paymentId, String item) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.item = item;
    }
}
