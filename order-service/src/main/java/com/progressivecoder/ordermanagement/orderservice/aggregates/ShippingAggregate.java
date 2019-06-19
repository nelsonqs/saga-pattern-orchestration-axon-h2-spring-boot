package com.progressivecoder.ordermanagement.orderservice.aggregates;

import com.progressivecoder.ordermanagement.orderservice.commands.CreateShippingCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.RejectedShippingCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.RollbackShippingCommand;
import com.progressivecoder.ordermanagement.orderservice.events.OrderShippedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.RejectedShippingEvent;
import com.progressivecoder.ordermanagement.orderservice.events.RollbackShippingEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class ShippingAggregate {

    @AggregateIdentifier
    private String shippingId;
    private String orderId;
    private String paymentId;
    private String itemType;
    private String ammount;
    private String rejected;
    private String rollback;

    public ShippingAggregate() {
    }

    @CommandHandler
    public ShippingAggregate(CreateShippingCommand createShippingCommand){
        AggregateLifecycle.apply(new OrderShippedEvent(createShippingCommand.shippingId, createShippingCommand.orderId, createShippingCommand.paymentId, createShippingCommand.itemType, createShippingCommand.ammount, createShippingCommand.rejected, createShippingCommand.rollback));
    }

    @EventSourcingHandler
    protected void on(OrderShippedEvent orderShippedEvent){
        this.shippingId = orderShippedEvent.shippingId;
        this.orderId = orderShippedEvent.orderId;
        this.paymentId = orderShippedEvent.paymentId;
        this.itemType = orderShippedEvent.itemType;
        this.ammount = orderShippedEvent.ammount;
        this.rejected = orderShippedEvent.rejected;
        this.rollback = orderShippedEvent.rollback;
    }

    @CommandHandler
    protected void on(RejectedShippingCommand createOrderCommand){
        AggregateLifecycle.apply(new RejectedShippingEvent(createOrderCommand.shippingId, createOrderCommand.orderId, createOrderCommand.paymentId, createOrderCommand.itemType, createOrderCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(RejectedShippingEvent rejectedShippingEvent){
        this.shippingId = rejectedShippingEvent.shippingId;
        this.paymentId = rejectedShippingEvent.paymentId;
        this.orderId = rejectedShippingEvent.orderId;
        this.itemType = rejectedShippingEvent.itemType;
        this.ammount = rejectedShippingEvent.ammount;
    }

    @CommandHandler
    protected void on(RollbackShippingCommand rollbackShippingCommand){
        AggregateLifecycle.apply(new RollbackShippingEvent(rollbackShippingCommand.shippingId, rollbackShippingCommand.orderId, rollbackShippingCommand.paymentId, rollbackShippingCommand.itemType));
    }

    @EventSourcingHandler
    protected void on(RollbackShippingEvent rollbackShippingEvent){
        this.shippingId = rollbackShippingEvent.shippingId;
        this.paymentId = rollbackShippingEvent.paymentId;
        this.orderId = rollbackShippingEvent.orderId;
        this.itemType = rollbackShippingEvent.itemType;
    }

}
