package com.progressivecoder.shippingmanagement.shippingservice.aggregates;

import com.progressivecoder.shippingmanagement.shippingservice.command.RejectedShippingCommand;
import com.progressivecoder.shippingmanagement.shippingservice.event.RejectedShippedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class RejectedShippingAggregate {

    @AggregateIdentifier
    private String shippingId;

    private String orderId;

    private String paymentId;

    private String item;

    public RejectedShippingAggregate() {
    }

    @CommandHandler
    public RejectedShippingAggregate(RejectedShippingCommand rejectedShippingCommand){
        AggregateLifecycle.apply(new RejectedShippedEvent(rejectedShippingCommand.shippingId, rejectedShippingCommand.orderId, rejectedShippingCommand.paymentId, rejectedShippingCommand.item));
    }

    @EventSourcingHandler
    protected void on(RejectedShippedEvent rejectedShippedEvent){
        this.shippingId = rejectedShippedEvent.shippingId;
        this.orderId = rejectedShippedEvent.orderId;
        this.paymentId = rejectedShippedEvent.paymentId;
        this.item = rejectedShippedEvent.item;
    }
}
