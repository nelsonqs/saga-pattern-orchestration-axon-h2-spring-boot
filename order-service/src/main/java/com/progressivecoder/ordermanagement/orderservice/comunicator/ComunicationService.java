package com.progressivecoder.ordermanagement.orderservice.comunicator;


public interface ComunicationService {
    String putCommand(String payment, String order,String ammount);
    String putCommandShipping(String shipping, String payment, String order, String type);
}
