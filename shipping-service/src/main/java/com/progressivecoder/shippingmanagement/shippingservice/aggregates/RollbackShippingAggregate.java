package com.progressivecoder.shippingmanagement.shippingservice.aggregates;

import com.progressivecoder.shippingmanagement.shippingservice.command.RollbackShippingCommand;
import com.progressivecoder.shippingmanagement.shippingservice.event.RollbackShippedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class RollbackShippingAggregate {

    @AggregateIdentifier
    private String shippingId;

    private String orderId;

    private String paymentId;

    private String item;

    public RollbackShippingAggregate() {
    }

    @CommandHandler
    public RollbackShippingAggregate(RollbackShippingCommand rollbackShippingCommand){
        AggregateLifecycle.apply(new RollbackShippedEvent(rollbackShippingCommand.shippingId, rollbackShippingCommand.orderId, rollbackShippingCommand.paymentId, rollbackShippingCommand.item));
    }

    @EventSourcingHandler
    protected void on(RollbackShippedEvent orderShippedEvent){
        this.shippingId = orderShippedEvent.shippingId;
        this.orderId = orderShippedEvent.orderId;
        this.paymentId = orderShippedEvent.paymentId;
        this.item = orderShippedEvent.item;
    }
}
