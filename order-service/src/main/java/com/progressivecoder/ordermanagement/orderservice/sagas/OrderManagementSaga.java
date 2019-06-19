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

        if (!orderCreatedEvent.rejected.isEmpty() && orderCreatedEvent.rejected.equalsIgnoreCase(Constants.ORDER_REJECTED_STATUS)){
          orderStatus = Constants.ORDER_REJECTED_STATUS;
        } else if (!orderCreatedEvent.rollback.isEmpty() && orderCreatedEvent.rollback.equalsIgnoreCase(Constants.ORDER_ROLLBACK_STATUS)){
          orderStatus = Constants.ORDER_ROLLBACK_STATUS;
        } else  {
          orderStatus = Constants.ORDER_CREATED_STATUS;
        }
        if (orderStatus.equalsIgnoreCase(Constants.ORDER_CREATED_STATUS)) {
            SagaLifecycle.associateWith("paymentId", paymentId);
            commandGateway.send(new CreatePaymentCommand(paymentId, orderCreatedEvent.orderId, orderCreatedEvent.itemType, String.valueOf(orderCreatedEvent.price), orderCreatedEvent.rejected, orderCreatedEvent.rollback));
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

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(PaymentCreatedEvent paymentCreatedEvent) {
        String shippingId = UUID.randomUUID().toString();
        String paymmentStatus;
        if (!paymentCreatedEvent.rejected.isEmpty() && paymentCreatedEvent.rejected.equalsIgnoreCase(Constants.PAYMENT_REJECTED)){
            paymmentStatus = Constants.PAYMENT_REJECTED;
        }else if (!paymentCreatedEvent.rollback.isEmpty() && paymentCreatedEvent.rollback.equalsIgnoreCase(Constants.PAYMENT_ROLLBACK)){
            paymmentStatus = Constants.PAYMENT_ROLLBACK;
        }else {
            paymmentStatus = Constants.PAYMENT_APPROVED;
        }
        if (paymmentStatus.equalsIgnoreCase(Constants.PAYMENT_APPROVED)) {
            String paymentMSId = comunicationService.paymentRest(paymentCreatedEvent.paymentId, paymentCreatedEvent.orderId, paymentCreatedEvent.ammount, paymmentStatus);
            SagaLifecycle.associateWith("shippingId", shippingId);
            log.info("Result OK!!!, to call << Payment >> Micro Service: ");
            commandGateway.send(new CreateShippingCommand(shippingId, paymentCreatedEvent.orderId, paymentCreatedEvent.paymentId, paymentCreatedEvent.item, paymentCreatedEvent.ammount, paymentCreatedEvent.rejected, paymentCreatedEvent.rollback));
        } else if (paymmentStatus.equalsIgnoreCase(Constants.PAYMENT_REJECTED)) {
            log.info("Rejected ...  ");
            commandGateway.send(new RejectedPaymentCommand(paymentCreatedEvent.paymentId, paymentCreatedEvent.orderId, paymentCreatedEvent.item, paymentCreatedEvent.ammount));
        } else if (paymmentStatus.equalsIgnoreCase(Constants.PAYMENT_ROLLBACK)) {
            log.info("Rollback ...  ");
            commandGateway.send(new RollbackPaymentCommand(paymentCreatedEvent.paymentId, paymentCreatedEvent.orderId, paymentCreatedEvent.item, paymentCreatedEvent.ammount));
        }
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(OrderShippedEvent orderShippedEvent) {
        String statusShipping;
        statusShipping = Constants.SHIPPING_APPROVED;
        //statusShipping = Constants.SHIPPING_REJECTED;
        //statusShipping = Constants.SHIPPING_ROLLBACK;
        if (statusShipping.equalsIgnoreCase(Constants.SHIPPING_APPROVED)) {
            String shippingMSId = comunicationService.putCommandShipping(orderShippedEvent.shippingId, orderShippedEvent.paymentId, orderShippedEvent.orderId,orderShippedEvent.itemType, statusShipping);
            log.info("Result OK!!!, to call << Shipping >> Micro Service: ");
            commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
        } else if (statusShipping.equalsIgnoreCase(Constants.SHIPPING_REJECTED)) {
            log.info("Rejected ...  ");
            commandGateway.send(new RejectedShippingCommand(orderShippedEvent.shippingId, orderShippedEvent.orderId, orderShippedEvent.paymentId, orderShippedEvent.itemType, orderShippedEvent.ammount));
        } else if (statusShipping.equalsIgnoreCase(Constants.SHIPPING_ROLLBACK)) {
            log.info("Rollback ...  ");
            commandGateway.send(new RollbackShippingCommand(orderShippedEvent.shippingId, orderShippedEvent.orderId, orderShippedEvent.paymentId, orderShippedEvent.itemType));
        }
        log.info("Start Update Order delivered with Id " + orderShippedEvent.orderId);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent) {
        log.info("============================== ");
        log.info("End Saga, bye  !!! ");
        log.info("============================== ");
        SagaLifecycle.end();
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

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(RollbackOrderEvent rollbackOrderEvent) {
        log.info("============================== ");
        log.info("Rollback Order !!!!  ");
        log.info("============================== ");
        deleteOrderRepository(rollbackOrderEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rollbackOrderEvent.orderId);
        SagaLifecycle.end();
    }


    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(RejectedPaymentEvent rejectedOrderEvent) {
        log.info("============================== ");
        log.info("Rejected Payment !!!!  ");
        log.info("============================== ");
        String paymmentStatus = Constants.PAYMENT_REJECTED;
        comunicationService.paymentRest(rejectedOrderEvent.paymentId, rejectedOrderEvent.orderId, rejectedOrderEvent.ammount, paymmentStatus);
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
        comunicationService.paymentRest(rollbackPaymentEvent.paymentId, rollbackPaymentEvent.orderId, rollbackPaymentEvent.ammount, paymmentStatus);
        SagaLifecycle.removeAssociationWith("paymentId", rollbackPaymentEvent.paymentId);
        deleteOrderRepository(rollbackPaymentEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rollbackPaymentEvent.orderId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(RejectedShippingEvent rejectedOrderEvent) {
        log.info("============================== ");
        log.info("Rejected Shipping !!!!  ");
        log.info("============================== ");
        String shippingStatus = Constants.SHIPPING_REJECTED;
        comunicationService.putCommandShipping(rejectedOrderEvent.shippingId, rejectedOrderEvent.paymentId, rejectedOrderEvent.orderId, rejectedOrderEvent.itemType, shippingStatus);
        SagaLifecycle.removeAssociationWith("shippingId", rejectedOrderEvent.shippingId);

        String paymmentStatus = Constants.PAYMENT_REJECTED;
        comunicationService.paymentRest(rejectedOrderEvent.paymentId, rejectedOrderEvent.orderId, rejectedOrderEvent.ammount, paymmentStatus);
        SagaLifecycle.removeAssociationWith("paymentId", rejectedOrderEvent.paymentId);
        updateOrderRepositoryWithRejectStatus(rejectedOrderEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rejectedOrderEvent.orderId);
        SagaLifecycle.end();
    }

    @SagaEventHandler(associationProperty = "shippingId")
    public void handle(RollbackShippingEvent rollbackShippingEvent) {
        log.info("============================== ");
        log.info("Rollback Shipping !!!!  ");
        log.info("============================== ");
        String shippingStatus = Constants.SHIPPING_ROLLBACK;
        comunicationService.putCommandShipping(rollbackShippingEvent.shippingId, rollbackShippingEvent.paymentId, rollbackShippingEvent.orderId, rollbackShippingEvent.itemType, shippingStatus);
        SagaLifecycle.removeAssociationWith("shippingId", rollbackShippingEvent.shippingId);
        String paymmentStatus = Constants.PAYMENT_ROLLBACK;
        comunicationService.paymentRest(rollbackShippingEvent.paymentId, rollbackShippingEvent.orderId, String.valueOf("1000000"), paymmentStatus);
        SagaLifecycle.removeAssociationWith("paymentId", rollbackShippingEvent.paymentId);
        deleteOrderRepository(rollbackShippingEvent.orderId);
        SagaLifecycle.removeAssociationWith("orderId", rollbackShippingEvent.orderId);
        SagaLifecycle.end();
    }

    private void deleteOrderRepository(String orderId) {
        if (orderRepository.findById(orderId).isPresent()) {
            OrderCreateEntity shippingEntity = orderRepository.findById(orderId).get();
            orderRepository.delete(shippingEntity);
        }
    }

    private void updateOrderRepositoryWithRejectStatus(String orderId) {
        OrderCreateEntity orderRejected =  orderRepository.findById(orderId).isPresent() ? orderRepository.findById(orderId).get() : new OrderCreateEntity();
        if (orderRejected != null) {
            orderRejected.setStatus(Constants.ORDER_REJECTED_STATUS);
            orderRepository.save(orderRejected);
        }
    }


}
