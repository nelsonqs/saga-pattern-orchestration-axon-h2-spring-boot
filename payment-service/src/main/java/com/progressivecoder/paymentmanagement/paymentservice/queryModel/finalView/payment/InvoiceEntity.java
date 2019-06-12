package com.progressivecoder.paymentmanagement.paymentservice.queryModel.finalView.payment;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class InvoiceEntity {
    @Id
    private String paymentId;
    private String orderId;
    private String invoiceStatus;

    public InvoiceEntity() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setInvoiceStatus(String invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }
}
