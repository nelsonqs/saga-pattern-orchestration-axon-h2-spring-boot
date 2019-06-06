//package com.progressivecoder.ordermanagement.orderservice.comunicator;
//
//import com.google.gson.JsonParser;
//import com.progressivecoder.ordermanagement.orderservice.config.Constants;
//import com.progressivecoder.ordermanagement.orderservice.commands.InvoiceCreateDTO;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.*;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.text.MessageFormat;
//
//@Service
//public class ComunicationServiceImpl implements ComunicationService {
//
//    private RestTemplate restTemplate;
//
//    public ComunicationServiceImpl(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    @Override
//    public JSONObject putCommand(String payment, String order) {
//        JSONObject info = new JSONObject();
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//
//        InvoiceCreateDTO invoiceCreateDTO = new InvoiceCreateDTO();
//        invoiceCreateDTO.setOrderId(order);
//        invoiceCreateDTO.setPaymentId(payment);
//
//        HttpEntity<?> entity = new HttpEntity<>(invoiceCreateDTO, headers);
//
//        ResponseEntity<String> response = null;
//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        try {
//            //todo call to discovery to obtain Link Schemma
//            String formatted_URL = MessageFormat.format(Constants.LINK_PAYMENT_SERVICES , payment, order);
//            response = restTemplate.exchange(formatted_URL, HttpMethod.POST, entity, String.class);
//        } catch (HttpServerErrorException e) {
//
//        }
//        if (response.getStatusCode() == HttpStatus.OK) {
//            try {
//                JsonParser parser = new JsonParser();
//                parser.parse(response.getBody());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                JSONObject jsonObject = new JSONObject(response.getBody());
//                if (!jsonObject.isNull("content")) {
//                    if (jsonObject.getJSONObject("content") instanceof JSONObject){
//                        info = new JSONObject(response.getBody()).getJSONObject("content");
//                        return info;
//                    }
//                } else if (!jsonObject.isNull("errors")) {
//
//                }
//            } catch (JSONException e) {
//               e.printStackTrace();
//            }
//        } else {
//            System.out.println(response);
//        }
//        return null;
//    }
//}
