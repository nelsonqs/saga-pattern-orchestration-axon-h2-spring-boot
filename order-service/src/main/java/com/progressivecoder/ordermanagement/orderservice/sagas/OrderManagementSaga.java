package com.progressivecoder.ordermanagement.orderservice.sagas;

import com.progressivecoder.ordermanagement.orderservice.aggregates.OrderStatus;
import com.progressivecoder.ordermanagement.orderservice.commands.CreateInvoiceCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.CreateShippingCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.UpdateOrderStatusCommand;
import com.progressivecoder.ordermanagement.orderservice.comunicator.ComunicationService;
import com.progressivecoder.ordermanagement.orderservice.config.Constants;
import com.progressivecoder.ordermanagement.orderservice.events.InvoiceCreatedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.OrderCreatedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.OrderShippedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.OrderUpdatedEvent;
import com.progressivecoder.ordermanagement.orderservice.OrderCreateEntity;
import com.progressivecoder.ordermanagement.orderservice.repository.OrderRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


@Saga
public class OrderManagementSaga {
    private static final Logger log = LoggerFactory.getLogger(OrderManagementSaga.class);

    @Inject
    private transient CommandGateway commandGateway;
    @Inject
    private transient ComunicationService comunicationService;
    @Inject
    private transient OrderRepository orderRepository;


    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent orderCreatedEvent) throws TimeoutException {
        String paymentId = UUID.randomUUID().toString();
        log.debug(" Start Saga !!!!  ");
        log.debug(" Start Order with Id ", orderCreatedEvent.orderId);
        SagaLifecycle.associateWith("paymentId", paymentId);
        OrderCreateEntity orderCreateEntity =  orderRepository.findById(orderCreatedEvent.orderId).isPresent() ? orderRepository.findById(orderCreatedEvent.orderId).get() : new OrderCreateEntity();
        if (orderCreateEntity != null) {
            orderCreateEntity.setOrderId( orderCreatedEvent.orderId);
            orderCreateEntity.setCurrency(orderCreatedEvent.currency);
            orderCreateEntity.setPrice(String.valueOf(orderCreatedEvent.price));
            orderCreateEntity.setItem(orderCreatedEvent.itemType);
            orderCreateEntity.setStatus(Constants.ORDER_CREATED_STATUS);
            orderRepository.save(orderCreateEntity);
        }
        String paymentMSId = comunicationService.putCommand(paymentId, orderCreatedEvent.orderId, String.valueOf(orderCreatedEvent.price));
        log.debug("Result to call MicroServices Paymment: ", paymentMSId);
        if (paymentMSId != null && !paymentMSId.isEmpty()) {
            log.debug("Result OK!!!, to call << Payment >> Micro Service: ", paymentMSId);
            commandGateway.send(new CreateInvoiceCommand(paymentId, orderCreatedEvent.orderId, orderCreatedEvent.itemType));
        } else {
            log.error("Result ERROR!!!, to call to <<Paymment>> Micro Service: ", paymentMSId);
            compensateOrder(orderCreatedEvent.orderId);
        }
    }

    private void compensateOrder(String orderId) {
       log.debug(" Call to Compensate ORDER, aborted Mission X) ", orderId);
       SagaLifecycle.removeAssociationWith("orderId", orderId);
       SagaLifecycle.end();
    }


    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(InvoiceCreatedEvent invoiceCreatedEvent) {
        String shippingId = UUID.randomUUID().toString();
        log.debug(" Start Payment with Id ", invoiceCreatedEvent.paymentId);
        //associate Saga with shipping
        SagaLifecycle.associateWith("shipping", shippingId);
        String shippingMSId = comunicationService.putCommandShipping(shippingId, invoiceCreatedEvent.paymentId, invoiceCreatedEvent.orderId,invoiceCreatedEvent.item);
        System.out.println("PaymentMicroServiceId: " + shippingMSId);
        if (shippingMSId != null && !shippingMSId.isEmpty()) {
            log.debug("Result OK!!!, to call to << Shipping >>  Micro Service: ", shippingMSId);
            commandGateway.send(new CreateShippingCommand(shippingId, invoiceCreatedEvent.orderId, invoiceCreatedEvent.paymentId));
        } else {
            log.error("Result ERROR!!!, to call to << Shipping >> Micro Service: ", shippingMSId);
            compensatePayment(invoiceCreatedEvent.paymentId,invoiceCreatedEvent.orderId );
        }
    }

    private void compensatePayment(String paymentId, String orderId) {
        log.error(" Call to COMPENSATE PAYMENT, aborted Mission X) order ", orderId, " payment id: ", paymentId);
        SagaLifecycle.removeAssociationWith("paymentId", paymentId);
        SagaLifecycle.removeAssociationWith("orderId", orderId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent) {
        log.debug(" Start Update Order delivered with Id ", orderShippedEvent.orderId);
        OrderCreateEntity orderCreateEntity =  orderRepository.findById(orderShippedEvent.orderId).isPresent() ? orderRepository.findById(orderShippedEvent.orderId).get() : new OrderCreateEntity();
        if (orderCreateEntity != null ) {
            log.debug("Result find order registered ID Order  : ", orderCreateEntity.getOrderId(), " with item ->", orderCreateEntity.getItem());
            orderCreateEntity.setStatus(Constants.ORDER_DELIVERED_STATUS);
            orderRepository.save(orderCreateEntity);
        } else {
            log.debug("wait momment please : ");
        }
//        else {
//            compensateShipment(orderShippedEvent.orderId,orderShippedEvent.paymentId, orderShippedEvent.shippingId);
//        }
        commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
    }

    private void compensateShipment(String orderId, String paymentId, String shippingId) {
        log.error(" Call to COMPENSATE SHIPPMENT, aborted Mission X) order ID", orderId, " payment id: ", paymentId, "shipping id", shippingId);
        SagaLifecycle.removeAssociationWith("orderId", orderId);
        SagaLifecycle.removeAssociationWith("paymentId", paymentId);
        SagaLifecycle.removeAssociationWith("shipping", shippingId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent) {
        log.debug(" End Saga, bye  !!! ");
        SagaLifecycle.end();
    }
}
