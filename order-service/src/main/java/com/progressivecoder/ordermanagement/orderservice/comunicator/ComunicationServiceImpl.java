package com.progressivecoder.ordermanagement.orderservice.comunicator;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.progressivecoder.ordermanagement.orderservice.commands.ShippingCreateDTO;
import com.progressivecoder.ordermanagement.orderservice.config.Constants;
import com.progressivecoder.ordermanagement.orderservice.commands.InvoiceCreateDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;

@Service
public class ComunicationServiceImpl implements ComunicationService {
    private RestTemplate restTemplate;

    public ComunicationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String putCommand(String payment, String order, String ammount) {
        JSONObject info = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        InvoiceCreateDTO invoiceCreateDTO = new InvoiceCreateDTO();
        invoiceCreateDTO.setOrderId(order);
        invoiceCreateDTO.setPaymentId(payment);
        invoiceCreateDTO.setAmmount(ammount);
        HttpEntity<?> entity = new HttpEntity<>(invoiceCreateDTO, headers);

        ResponseEntity<String> response;
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            //todo call to discovery to obtain Link Schemma
            String formatted_URL = MessageFormat.format(Constants.LINK_PAYMENT_SERVICES, payment, order,ammount );
            response = restTemplate.exchange(formatted_URL, HttpMethod.POST, entity, String.class);
        }
        catch (HttpServerErrorException e) {
            e.printStackTrace();
            return null;
        }catch (RestClientException r) {
            r.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonParser parser = new JsonParser();
                parser.parse(response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            try {
                Gson gson = new Gson();
                String dsdsd = gson.toJson(response.getBody());
                return dsdsd;
//                JSONObject jsonObject = new JSONObject(response.getBody());
//                if (!jsonObject.isNull("content")) {
//                    if (jsonObject.getJSONObject("content") instanceof JSONObject){
//                        info = new JSONObject(response.getBody()).getJSONObject("content");
//                        return info;
//                    }
//                } else if (!jsonObject.isNull("errors")) {
//
//                }
            } catch (JSONException e) {
               e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("DIO UN NOOOOK------------NOOK "+response);
            return null;
        }
    }

    @Override
    public String putCommandShipping(String shipping, String payment, String order, String item) {
        JSONObject info = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        ShippingCreateDTO shippingCreateDTO = new ShippingCreateDTO();
        shippingCreateDTO.setShippingId(shipping);
        shippingCreateDTO.setPaymentId(payment);
        shippingCreateDTO.setOrderId(order);
        shippingCreateDTO.setItem(item);
        HttpEntity<?> entity = new HttpEntity<>(shippingCreateDTO, headers);

        ResponseEntity<String> response;
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        try {
            //todo call to discovery to obtain Link Schemma
            String formatted_URL = MessageFormat.format(Constants.LINK_SHIPPING_SERVICES, shipping,order,payment,item);
            response = restTemplate.exchange(formatted_URL, HttpMethod.POST, entity, String.class);
        }
        catch (HttpServerErrorException e) {
            e.printStackTrace();
            return null;
        }catch (RestClientException r) {
            r.printStackTrace();
            return null;
        }
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                JsonParser parser = new JsonParser();
                parser.parse(response.getBody());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            try {
                Gson gson = new Gson();
                String dsdsd = gson.toJson(response.getBody());
                return dsdsd;
//                JSONObject jsonObject = new JSONObject(response.getBody());
//                if (!jsonObject.isNull("content")) {
//                    if (jsonObject.getJSONObject("content") instanceof JSONObject){
//                        info = new JSONObject(response.getBody()).getJSONObject("content");
//                        return info;
//                    }
//                } else if (!jsonObject.isNull("errors")) {
//
//                }
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("DIO UN NOOOOK shipping------------NOOK "+response);
            return null;
        }
    }

}
