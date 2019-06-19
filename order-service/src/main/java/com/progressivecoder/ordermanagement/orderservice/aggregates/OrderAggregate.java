package com.progressivecoder.ordermanagement.orderservice.aggregates;

import com.progressivecoder.ordermanagement.orderservice.commands.CreateOrderCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.RejectedOrderCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.RollbackOrderCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.UpdateOrderStatusCommand;
import com.progressivecoder.ordermanagement.orderservice.events.OrderCreatedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.OrderUpdatedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.RejectedOrderEvent;
import com.progressivecoder.ordermanagement.orderservice.events.RollbackOrderEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;

    private ItemType itemType;

    private BigDecimal price;

    private String currency;

    private OrderStatus orderStatus;

    private String rejected;

    private String rollback;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand){
        AggregateLifecycle.apply(new OrderCreatedEvent(createOrderCommand.orderId, createOrderCommand.itemType,
                createOrderCommand.price, createOrderCommand.currency, createOrderCommand.orderStatus, createOrderCommand.rejected, createOrderCommand.rollback));
    }

    @EventSourcingHandler
    protected void on(OrderCreatedEvent orderCreatedEvent){
        this.orderId = orderCreatedEvent.orderId;
        this.itemType = ItemType.valueOf(orderCreatedEvent.itemType);
        this.price = orderCreatedEvent.price;
        this.currency = orderCreatedEvent.currency;
        this.orderStatus = OrderStatus.valueOf(orderCreatedEvent.orderStatus);
        this.rejected = orderCreatedEvent.rejected;
        this.rollback = orderCreatedEvent.rollback;
    }

    @CommandHandler
    protected void on(UpdateOrderStatusCommand updateOrderStatusCommand){
        AggregateLifecycle.apply(new OrderUpdatedEvent(updateOrderStatusCommand.orderId, updateOrderStatusCommand.orderStatus));
    }

    @EventSourcingHandler
    protected void on(OrderUpdatedEvent orderUpdatedEvent){
        this.orderId = orderId;
        this.orderStatus = OrderStatus.valueOf(orderUpdatedEvent.orderStatus);
    }

    @CommandHandler
    protected void on(RollbackOrderCommand rollbackOrderCommand){
        AggregateLifecycle.apply(new RollbackOrderEvent(rollbackOrderCommand.orderId, rollbackOrderCommand.orderStatus));
    }

    @EventSourcingHandler
    protected void on(RollbackOrderEvent rollbackOrderEvent){
        this.orderId = orderId;
        this.orderStatus = OrderStatus.valueOf(rollbackOrderEvent.orderStatus);
    }

    @CommandHandler
    protected void on(RejectedOrderCommand createOrderCommand){
        AggregateLifecycle.apply(new RejectedOrderEvent(createOrderCommand.orderId, createOrderCommand.itemType,
                createOrderCommand.price, createOrderCommand.currency, createOrderCommand.orderStatus));
    }

    @EventSourcingHandler
    protected void on(RejectedOrderEvent rollbackOrderEvent){
        this.orderId = rollbackOrderEvent.orderId;
        this.itemType = ItemType.valueOf(rollbackOrderEvent.itemType);
        this.price = rollbackOrderEvent.price;
        this.currency = rollbackOrderEvent.currency;
        this.orderStatus = OrderStatus.valueOf(rollbackOrderEvent.orderStatus);
    }

}
