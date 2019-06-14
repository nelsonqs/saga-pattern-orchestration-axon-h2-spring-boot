package com.progressivecoder.ordermanagement.orderservice.sagas;

import com.progressivecoder.ordermanagement.orderservice.OrderCreateEntity;
import com.progressivecoder.ordermanagement.orderservice.aggregates.OrderStatus;
import com.progressivecoder.ordermanagement.orderservice.commands.*;
import com.progressivecoder.ordermanagement.orderservice.comunicator.ComunicationService;
import com.progressivecoder.ordermanagement.orderservice.config.Constants;
import com.progressivecoder.ordermanagement.orderservice.events.*;
import com.progressivecoder.ordermanagement.orderservice.repository.OrderRepository;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.UUID;


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
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        String paymentId = UUID.randomUUID().toString();
        String orderStatus;
        log.info("============================== ");
        log.info("Start Saga !!!!  ");
        log.info("============================== ");
        log.info("Start Order ");
        OrderCreateEntity orderCreateEntity =  orderRepository.findById(orderCreatedEvent.orderId).isPresent() ? orderRepository.findById(orderCreatedEvent.orderId).get() : new OrderCreateEntity();
        if (orderCreateEntity != null) {
            orderCreateEntity.setOrderId( orderCreatedEvent.orderId);
            orderCreateEntity.setCurrency(orderCreatedEvent.currency);
            orderCreateEntity.setPrice(String.valueOf(orderCreatedEvent.price));
            orderCreateEntity.setItem(orderCreatedEvent.itemType);
            orderCreateEntity.setStatus(Constants.ORDER_CREATED_STATUS);
            orderRepository.save(orderCreateEntity);
            log.info("Order created Id " + orderCreatedEvent.orderId);
        }
        orderStatus = Constants.ORDER_CREATED_STATUS;
        //orderStatus = Constants.ORDER_REJECTED_STATUS;
        //orderStatus = Constants.ORDER_ROLLBACK_STATUS;
        if (orderStatus.equalsIgnoreCase(Constants.ORDER_CREATED_STATUS)) {
            SagaLifecycle.associateWith("paymentId", paymentId);
            log.info("Result OK!!!, to call << Payment >> Micro Service: ");
            commandGateway.send(new CreatePaymentCommand(paymentId, orderCreatedEvent.orderId, orderCreatedEvent.itemType));
        } else if (orderStatus.equalsIgnoreCase(Constants.ORDER_REJECTED_STATUS)) {
            log.info("Rejected ...  ");
            commandGateway.send(new RejectedOrderCommand(orderCreatedEvent.orderId, orderCreatedEvent.itemType,
                                                         orderCreatedEvent.price,orderCreatedEvent.currency, orderCreatedEvent.orderStatus));
        } else if (orderStatus.equalsIgnoreCase(Constants.ORDER_ROLLBACK_STATUS)) {
            log.info("Rollback ...  ");
            commandGateway.send(new RollbackOrderCommand(orderCreatedEvent.orderId, orderCreatedEvent.itemType,
                    orderCreatedEvent.price,orderCreatedEvent.currency, orderCreatedEvent.orderStatus));
        }
    }
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(RollbackOrderEvent rollbackOrderEvent) {
        log.info("============================== ");
        log.info("Rollback Order !!!!  ");
        log.info("============================== ");
        OrderCreateEntity orderCreateEntity =  orderRepository.findById(rollbackOrderEvent.orderId).isPresent() ? orderRepository.findById(rollbackOrderEvent.orderId).get() : new OrderCreateEntity();
        if (orderCreateEntity != null) {
            orderRepository.delete(orderCreateEntity);
            SagaLifecycle.removeAssociationWith("orderId", rollbackOrderEvent.orderId);
            SagaLifecycle.end();
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(RejectedOrderEvent rejectedOrderEvent) {
        log.info("============================== ");
        log.info("Rejected Order !!!!  ");
        log.info("============================== ");
        updateOrderRepositoryWithRejectStatus(rejectedOrderEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rejectedOrderEvent.orderId);
        SagaLifecycle.end();
    }


    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(PaymentCreatedEvent paymentCreatedEvent) {
        String shippingId = UUID.randomUUID().toString();
        String paymmentStatus;
        paymmentStatus = Constants.PAYMENT_APPROVED;
        //paymmentStatus = Constants.PAYMENT_REJECTED;
        //paymmentStatus = Constants.PAYMENT_ROLLBACK;
        if (paymmentStatus.equalsIgnoreCase(Constants.PAYMENT_APPROVED)) {
            String paymentMSId = comunicationService.paymentRest(paymentCreatedEvent.paymentId, paymentCreatedEvent.orderId, String.valueOf("1000000"), paymmentStatus);
            SagaLifecycle.associateWith("shipping", shippingId);
            log.info("Result OK!!!, to call << Shipping >> Micro Service: ");
            commandGateway.send(new CreateShippingCommand(shippingId, paymentCreatedEvent.orderId, paymentCreatedEvent.paymentId, paymentCreatedEvent.item));
        } else if (paymmentStatus.equalsIgnoreCase(Constants.PAYMENT_REJECTED)) {
            log.info("Rejected ...  ");
            commandGateway.send(new RejectedPaymentCommand(paymentCreatedEvent.paymentId, paymentCreatedEvent.orderId, paymentCreatedEvent.item));
        } else if (paymmentStatus.equalsIgnoreCase(Constants.PAYMENT_ROLLBACK)) {
            log.info("Rollback ...  ");
            commandGateway.send(new RollbackPaymentCommand(paymentCreatedEvent.paymentId, paymentCreatedEvent.orderId,paymentCreatedEvent.item));
        }
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(RejectedPaymentEvent rejectedOrderEvent) {
        log.info("============================== ");
        log.info("Rejected Payment !!!!  ");
        log.info("============================== ");
        String paymmentStatus = Constants.PAYMENT_REJECTED;
        comunicationService.paymentRest(rejectedOrderEvent.paymentId, rejectedOrderEvent.orderId, String.valueOf("1000000"), paymmentStatus);
        SagaLifecycle.removeAssociationWith("paymentId", rejectedOrderEvent.paymentId);
        updateOrderRepositoryWithRejectStatus(rejectedOrderEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rejectedOrderEvent.orderId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(RollbackPaymentEvent rollbackPaymentEvent) {
        log.info("============================== ");
        log.info("Rollback Payment !!!!  ");
        log.info("============================== ");
        String paymmentStatus = Constants.PAYMENT_ROLLBACK;
        comunicationService.paymentRest(rollbackPaymentEvent.paymentId, rollbackPaymentEvent.orderId, String.valueOf("1000000"), paymmentStatus);
        SagaLifecycle.removeAssociationWith("paymentId", rollbackPaymentEvent.paymentId);
        OrderCreateEntity orderCreateEntity =  orderRepository.findById(rollbackPaymentEvent.orderId).isPresent() ? orderRepository.findById(rollbackPaymentEvent.orderId).get() : new OrderCreateEntity();
        if (orderCreateEntity != null) {
            orderRepository.delete(orderCreateEntity);
            SagaLifecycle.removeAssociationWith("orderId", rollbackPaymentEvent.orderId);
        }
        SagaLifecycle.end();
    }

    private void updateOrderRepositoryWithRejectStatus(String orderId) {
        OrderCreateEntity orderRejected =  orderRepository.findById(orderId).isPresent() ? orderRepository.findById(orderId).get() : new OrderCreateEntity();
        if (orderRejected != null) {
            orderRejected.setStatus(Constants.ORDER_REJECTED_STATUS);
            orderRepository.save(orderRejected);
        }
    }

    private void compensatePayment(String paymentId, String orderId) {
        log.error("Call to COMPENSATE PAYMENT, aborted Mission X) order ", orderId, " payment id: ", paymentId);
        SagaLifecycle.removeAssociationWith("paymentId", paymentId);
        SagaLifecycle.removeAssociationWith("orderId", orderId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "shipping")
    public void handle(OrderShippedEvent orderShippedEvent) {
        String statusShipping;
        statusShipping = Constants.SHIPPING_APPROVED;
        //statusShipping = Constants.SHIPPING_REJECTED;
        //statusShipping = Constants.SHIPPING_ROLLBACK;
        if (statusShipping.equalsIgnoreCase(Constants.PAYMENT_APPROVED)) {
            String shippingMSId = comunicationService.putCommandShipping(orderShippedEvent.shippingId, orderShippedEvent.paymentId, orderShippedEvent.orderId,orderShippedEvent.itemType, statusShipping);
            log.info("Result OK!!!, to call << Shipping >> Micro Service: ");
            commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
        } else if (statusShipping.equalsIgnoreCase(Constants.PAYMENT_REJECTED)) {
            log.info("Rejected ...  ");
            commandGateway.send(new RejectedShippingCommand(orderShippedEvent.shippingId, orderShippedEvent.orderId, orderShippedEvent.paymentId, orderShippedEvent.itemType));
        } else if (statusShipping.equalsIgnoreCase(Constants.PAYMENT_ROLLBACK)) {
            log.info("Rollback ...  ");
            commandGateway.send(new RollbackShippingCommand(orderShippedEvent.shippingId, orderShippedEvent.orderId, orderShippedEvent.paymentId, orderShippedEvent.itemType));
        }

        log.info("Start Update Order delivered with Id " + orderShippedEvent.orderId);
//        SagaLifecycle.associateWith("shipping", shippingId);
//        String statusShipping = Constants.SHIPPING_APPROVED;
//        String shippingMSId = comunicationService.putCommandShipping(shippingId, paymentCreatedEvent.paymentId, paymentCreatedEvent.orderId,paymentCreatedEvent.item, statusShipping);
//        OrderCreateEntity orderCreateEntity =  orderRepository.findById(orderShippedEvent.orderId).isPresent() ? orderRepository.findById(orderShippedEvent.orderId).get() : new OrderCreateEntity();
//        if (orderCreateEntity != null ) {
//            log.info("Result find order registered ID Order  : " + orderCreateEntity.getOrderId(), " with item ->", orderCreateEntity.getItem());
//            orderCreateEntity.setStatus(Constants.ORDER_DELIVERED_STATUS);
//            orderRepository.save(orderCreateEntity);
//        } else {
//            log.info("wait momment please : ");
//        }
//        else {
//            compensateShipment(orderShippedEvent.orderId,orderShippedEvent.paymentId, orderShippedEvent.shippingId);
//        }

    }

    @SagaEventHandler(associationProperty = "shipping")
    public void handle(RejectedShippingEvent rejectedOrderEvent) {
        log.info("============================== ");
        log.info("Rejected Shipping !!!!  ");
        log.info("============================== ");
        String shippingStatus = Constants.SHIPPING_REJECTED;
        comunicationService.putCommandShipping(rejectedOrderEvent.shippingId, rejectedOrderEvent.paymentId, rejectedOrderEvent.orderId, rejectedOrderEvent.itemType, shippingStatus);
        SagaLifecycle.removeAssociationWith("paymentId", rejectedOrderEvent.paymentId);
        updateOrderRepositoryWithRejectStatus(rejectedOrderEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rejectedOrderEvent.orderId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "shipping")
    public void handle(RollbackShippingEvent rollbackShippingEvent) {
        log.info("============================== ");
        log.info("Rejected Shipping !!!!  ");
        log.info("============================== ");
        String shippingStatus = Constants.SHIPPING_REJECTED;
        comunicationService.putCommandShipping(rollbackShippingEvent.shippingId, rollbackShippingEvent.paymentId, rollbackShippingEvent.orderId, rollbackShippingEvent.itemType, shippingStatus);
        SagaLifecycle.removeAssociationWith("paymentId", rollbackShippingEvent.paymentId);
        updateOrderRepositoryWithRejectStatus(rollbackShippingEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rollbackShippingEvent.orderId);
        SagaLifecycle.end();
    }


    private void compensateShipment(String orderId, String paymentId, String shippingId) {
        log.error("Call to COMPENSATE SHIPPMENT, aborted Mission X) order ID" + orderId + " payment id: " + paymentId, "shipping id", shippingId);
        SagaLifecycle.removeAssociationWith("orderId", orderId);
        SagaLifecycle.removeAssociationWith("paymentId", paymentId);
        SagaLifecycle.removeAssociationWith("shipping", shippingId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent) {
        log.info("End Saga, bye  !!! ");
        SagaLifecycle.end();
    }
}
