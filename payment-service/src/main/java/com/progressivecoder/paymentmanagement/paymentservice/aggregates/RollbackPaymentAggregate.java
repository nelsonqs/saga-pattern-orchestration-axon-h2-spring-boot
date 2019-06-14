package com.progressivecoder.paymentmanagement.paymentservice.aggregates;

import com.progressivecoder.paymentmanagement.paymentservice.command.RollbackPaymentCommand;
import com.progressivecoder.paymentmanagement.paymentservice.events.RollbackPaymentEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class RollbackPaymentAggregate {

    @AggregateIdentifier
    private String paymentId;

    private String orderId;

    private InvoiceStatus invoiceStatus;
    private String ammount;

    public RollbackPaymentAggregate() {
    }

    @CommandHandler
    public RollbackPaymentAggregate(RollbackPaymentCommand rollbackInvoiceCommand) {
        AggregateLifecycle.apply(new RollbackPaymentEvent(rollbackInvoiceCommand.paymentId, rollbackInvoiceCommand.orderId, rollbackInvoiceCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(RollbackPaymentEvent invoiceCreatedEvent) {
        this.paymentId = invoiceCreatedEvent.paymentId;
        this.orderId = invoiceCreatedEvent.orderId;
        this.invoiceStatus = InvoiceStatus.PAID;
        this.ammount = invoiceCreatedEvent.ammount;
    }
}
