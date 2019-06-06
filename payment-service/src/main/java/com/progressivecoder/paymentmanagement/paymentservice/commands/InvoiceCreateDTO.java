package com.progressivecoder.paymentmanagement.paymentservice.commands;

public class InvoiceCreateDTO {

    private String paymentId;
    private String orderId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
