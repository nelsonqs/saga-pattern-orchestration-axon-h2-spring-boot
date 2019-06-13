package com.progressivecoder.paymentmanagement.paymentservice.aggregates;

import com.progressivecoder.paymentmanagement.paymentservice.command.RejectedInvoiceCommand;
import com.progressivecoder.paymentmanagement.paymentservice.events.RejectedInvoiceEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class RejectedAggregate {

    @AggregateIdentifier
    private String paymentId;

    private String orderId;

    private InvoiceStatus invoiceStatus;
    private String ammount;

    public RejectedAggregate() {
    }

    @CommandHandler
    public RejectedAggregate(RejectedInvoiceCommand rejectedInvoiceCommand) {
        AggregateLifecycle.apply(new RejectedInvoiceEvent(rejectedInvoiceCommand.paymentId, rejectedInvoiceCommand.orderId, rejectedInvoiceCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(RejectedInvoiceEvent invoiceCreatedEvent) {
        this.paymentId = invoiceCreatedEvent.paymentId;
        this.orderId = invoiceCreatedEvent.orderId;
        this.invoiceStatus = InvoiceStatus.PAID;
        this.ammount = invoiceCreatedEvent.ammount;
    }
}
