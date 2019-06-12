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

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


@Saga
public class OrderManagementSaga {

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
        System.out.println("Saga invoked");
        //associate Saga
        SagaLifecycle.associateWith("paymentId", paymentId);
        System.out.println("order id" + orderCreatedEvent.orderId);
        OrderCreateEntity orderCreateEntity =  orderRepository.findById(orderCreatedEvent.orderId).isPresent() ? orderRepository.findById(orderCreatedEvent.orderId).get() : new OrderCreateEntity();
        if (orderCreateEntity != null) {
            orderCreateEntity.setOrderId( orderCreatedEvent.orderId);
            orderCreateEntity.setCurrency(orderCreatedEvent.currency);
            orderCreateEntity.setPrice(String.valueOf(orderCreatedEvent.price));
            orderCreateEntity.setItem(orderCreatedEvent.itemType);
            orderCreateEntity.setStatus(Constants.ORDER_CREATED_STATUS);
            orderRepository.save(orderCreateEntity);
        }
        //send the commands
        // call rest template
        String paymentMSId = comunicationService.putCommand(paymentId, orderCreatedEvent.orderId, String.valueOf(orderCreatedEvent.price));
        System.out.println("PaymentMicroServiceId: " + paymentMSId);
        if (paymentMSId != null && !paymentMSId.isEmpty()) {
            commandGateway.send(new CreateInvoiceCommand(paymentId, orderCreatedEvent.orderId, orderCreatedEvent.itemType));
        } else {
            SagaLifecycle.removeAssociationWith("paymentId", paymentId);
        }
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(InvoiceCreatedEvent invoiceCreatedEvent) {
        String shippingId = UUID.randomUUID().toString();
        System.out.println("Saga continued payment");
        //associate Saga with shipping
        SagaLifecycle.associateWith("shipping", shippingId);

        String paymentMSId = comunicationService.putCommandShipping(shippingId, invoiceCreatedEvent.paymentId, invoiceCreatedEvent.orderId,invoiceCreatedEvent.item);
        System.out.println("PaymentMicroServiceId: " + paymentMSId);
        if (paymentMSId != null && !paymentMSId.isEmpty()) {
            commandGateway.send(new CreateShippingCommand(shippingId, invoiceCreatedEvent.orderId, invoiceCreatedEvent.paymentId));
        } else {
            SagaLifecycle.removeAssociationWith("paymentId", shippingId);
        }

        //send the create shipping command

    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent) {
        System.out.println("Saga continued order");
        commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent) {
        System.out.println("Saga end");
        OrderCreateEntity orderCreateEntity =  orderRepository.findById(orderUpdatedEvent.orderId).isPresent() ? orderRepository.findById(orderUpdatedEvent.orderId).get() : new OrderCreateEntity();
        if (orderCreateEntity != null ) {
            orderCreateEntity.setStatus(Constants.ORDER_DELIVERED_STATUS);
            orderRepository.save(orderCreateEntity);
        }

        SagaLifecycle.end();
    }
}
