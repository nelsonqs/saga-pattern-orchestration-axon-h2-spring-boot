package com.progressivecoder.ordermanagement.orderservice.events;

public class RejectedShippingEvent {

    public final String shippingId;

    public final String orderId;

    public final String paymentId;

    public final String itemType;
    public final String ammount;


    public RejectedShippingEvent(String shippingId, String orderId, String paymentId, String itemType, String ammount) {
        this.shippingId = shippingId;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.itemType = itemType;
        this.ammount = ammount;
    }
}
