package com.progressivecoder.ordermanagement.orderservice.comunicator;

import com.google.gson.Gson;
import com.progressivecoder.ordermanagement.orderservice.commands.InvoiceCreateDTO;
import com.progressivecoder.ordermanagement.orderservice.commands.ShippingCreateDTO;
import com.progressivecoder.ordermanagement.orderservice.config.Constants;
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
    public String paymentRest(String payment, String order, String ammount, String status) {
        JSONObject info = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        InvoiceCreateDTO invoiceCreateDTO = new InvoiceCreateDTO();
        invoiceCreateDTO.setPaymentId(payment);
        invoiceCreateDTO.setOrderId(order);
        invoiceCreateDTO.setAmmount(ammount);
        HttpEntity<?> entity = new HttpEntity<>(invoiceCreateDTO, headers);

        ResponseEntity<String> response;
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        String formatted_URL = "";
        try {
            if (status.equalsIgnoreCase(Constants.PAYMENT_APPROVED)) {
                formatted_URL = MessageFormat.format(Constants.LINK_PAYMENT_SERVICES, payment, order, ammount);
            } else if (status.equalsIgnoreCase(Constants.PAYMENT_REJECTED)) {
                formatted_URL = MessageFormat.format(Constants.LINK_PAYMENT_REJECTED, payment, order, ammount);
            } else if (status.equalsIgnoreCase(Constants.PAYMENT_ROLLBACK)) {
                formatted_URL = MessageFormat.format(Constants.LINK_PAYMENT_ROLLBACK, payment, order, ammount);
            }
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
                Gson gson = new Gson();
                String dsdsd = gson.toJson(response.getBody());
                return dsdsd;
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
    public String putCommandShipping(String shipping, String payment, String order, String item, String status) {
        JSONObject info = new JSONObject();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        String formatted_URL = "";
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
            if (status.equalsIgnoreCase(Constants.SHIPPING_APPROVED)) {
                formatted_URL = MessageFormat.format(Constants.LINK_SHIPPING_SERVICES, shipping, order, payment, item);
            } else if (status.equalsIgnoreCase(Constants.SHIPPING_ROLLBACK)) {
                formatted_URL = MessageFormat.format(Constants.LINK_SHIPPING_ROLLBACK, shipping, order, payment, item);
            } else if (status.equalsIgnoreCase(Constants.SHIPPING_REJECTED)) {
                formatted_URL = MessageFormat.format(Constants.LINK_SHIPPING_REJECTED, shipping, order, payment, item);
            }
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
                Gson gson = new Gson();
                String dsdsd = gson.toJson(response.getBody());
                return dsdsd;
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
