package com.progressivecoder.ordermanagement.orderservice.sagas;

import com.progressivecoder.ordermanagement.orderservice.aggregates.OrderStatus;
import com.progressivecoder.ordermanagement.orderservice.commands.CreateInvoiceCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.CreateShippingCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.UpdateOrderStatusCommand;
import com.progressivecoder.ordermanagement.orderservice.comunicator.ComunicationService;
import com.progressivecoder.ordermanagement.orderservice.events.InvoiceCreatedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.OrderCreatedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.OrderShippedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.OrderUpdatedEvent;
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
//    @Autowired
//    ComunicationService comunicationService;


    @Inject
    private transient CommandGateway commandGateway;

    @Inject
    private transient ComunicationService comunicationService;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
//    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void handle(OrderCreatedEvent orderCreatedEvent) throws TimeoutException {
        String paymentId = UUID.randomUUID().toString();
        System.out.println("Saga invoked");
        //associate Saga
        SagaLifecycle.associateWith("paymentId", paymentId);
        System.out.println("order id" + orderCreatedEvent.orderId);
        //send the commands
        // call rest template
        String dataOrder = comunicationService.putCommand(paymentId,orderCreatedEvent.orderId);
        System.out.println("OrderCreatedEvent: "+dataOrder);

        commandGateway.send(new CreateInvoiceCommand(paymentId, orderCreatedEvent.orderId));
    }

    @SagaEventHandler(associationProperty = "paymentId")
    public void handle(InvoiceCreatedEvent invoiceCreatedEvent){
        String shippingId = UUID.randomUUID().toString();

        System.out.println("Saga continued payment");
        //para desasociar
        //SagaLifecycle.removeAssociationWith ("shipping", shippingId )
        //associate Saga with shipping
        SagaLifecycle.associateWith("shipping", shippingId);
        //send the create shipping command
        commandGateway.send(new CreateShippingCommand(shippingId, invoiceCreatedEvent.orderId, invoiceCreatedEvent.paymentId));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderShippedEvent orderShippedEvent){
        System.out.println("Saga continued order");
        commandGateway.send(new UpdateOrderStatusCommand(orderShippedEvent.orderId, String.valueOf(OrderStatus.SHIPPED)));
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderUpdatedEvent orderUpdatedEvent){
        System.out.println("Saga end");
        SagaLifecycle.end();
    }
}
