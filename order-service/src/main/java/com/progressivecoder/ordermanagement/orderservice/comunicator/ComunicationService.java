package com.progressivecoder.ordermanagement.orderservice.comunicator;


public interface ComunicationService {
    String paymentRest(String payment, String order, String ammount, String status);
    String putCommandShipping(String shipping, String payment, String order, String type);
}
