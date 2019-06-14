package com.progressivecoder.ordermanagement.orderservice.config;

public class Constants {
    public static final String LINK_PAYMENT_SERVICES = "http://localhost:8081/api/payment";
    public static final String LINK_PAYMENT_REJECTED = "http://localhost:8081/api/payment-rejected";
    public static final String LINK_PAYMENT_ROLLBACK = "http://localhost:8081/api/payment-rollback";

    public static final String LINK_SHIPPING_SERVICES = "http://localhost:8082/api/shipping";
    public static final String LINK_SHIPPING_ROLLBACK = "http://localhost:8082/api/shipping-rollback";
    public static final String LINK_SHIPPING_REJECTED = "http://localhost:8082/api/shipping-rollback";

    public static final String ORDER_CREATED_STATUS = "ORDER_CREATED";
    public static final String ORDER_REJECTED_STATUS = "ORDER_REJECTED";
    public static final String ORDER_ROLLBACK_STATUS = "ORDER_ROLLBACK";
    public static final String ORDER_DELIVERED_STATUS = "ORDER_DELIVERED";

    public static final String PAYMENT_REJECTED = "PAYMENT_REJECTED";
    public static final String PAYMENT_APPROVED = "PAYMENT_APPROVED";
    public static final String PAYMENT_ROLLBACK = "PAYMENT_ROLLBACK";

    public static final String SHIPPING_APPROVED = "SHIPPING_APPROVED";
    public static final String SHIPPING_ROLLBACK = "SHIPPING_ROLLBACK";
    public static final String SHIPPING_REJECTED = "SHIPPING_REJECTED";



}
