package com.progressivecoder.paymentmanagement.paymentservice.aggregates;

import com.progressivecoder.paymentmanagement.paymentservice.command.RejectedPaymentCommand;
import com.progressivecoder.paymentmanagement.paymentservice.events.RejectedPaymentEvent;
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
    public RejectedAggregate(RejectedPaymentCommand rejectedInvoiceCommand) {
        AggregateLifecycle.apply(new RejectedPaymentEvent(rejectedInvoiceCommand.paymentId, rejectedInvoiceCommand.orderId, rejectedInvoiceCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(RejectedPaymentEvent invoiceCreatedEvent) {
        this.paymentId = invoiceCreatedEvent.paymentId;
        this.orderId = invoiceCreatedEvent.orderId;
        this.invoiceStatus = InvoiceStatus.PAID;
        this.ammount = invoiceCreatedEvent.ammount;
    }
}
