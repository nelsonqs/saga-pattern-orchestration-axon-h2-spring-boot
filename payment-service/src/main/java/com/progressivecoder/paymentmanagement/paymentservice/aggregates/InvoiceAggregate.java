package com.progressivecoder.paymentmanagement.paymentservice.aggregates;

import com.progressivecoder.paymentmanagement.paymentservice.command.CreatePaymentCommand;
import com.progressivecoder.paymentmanagement.paymentservice.command.RejectedPaymentCommand;
import com.progressivecoder.paymentmanagement.paymentservice.command.RollbackPaymentCommand;
import com.progressivecoder.paymentmanagement.paymentservice.events.PaymentCreatedEvent;
import com.progressivecoder.paymentmanagement.paymentservice.events.RejectedPaymentEvent;
import com.progressivecoder.paymentmanagement.paymentservice.events.RollbackPaymentEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class InvoiceAggregate {

    @AggregateIdentifier
    private String paymentId;

    private String orderId;

    private InvoiceStatus invoiceStatus;
    private String  ammount;

    public InvoiceAggregate() {
    }

    @CommandHandler
    public InvoiceAggregate(CreatePaymentCommand createInvoiceCommand){
        AggregateLifecycle.apply(new PaymentCreatedEvent(createInvoiceCommand.paymentId, createInvoiceCommand.orderId, createInvoiceCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(PaymentCreatedEvent invoiceCreatedEvent){
        this.paymentId = invoiceCreatedEvent.paymentId;
        this.orderId = invoiceCreatedEvent.orderId;
        this.invoiceStatus = InvoiceStatus.PAID;
        this.ammount = invoiceCreatedEvent.ammount;
    }

    @CommandHandler
    public void on(RejectedPaymentCommand rejectedInvoiceCommand) {
        AggregateLifecycle.apply(new RejectedPaymentEvent(rejectedInvoiceCommand.paymentId, rejectedInvoiceCommand.orderId, rejectedInvoiceCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(RejectedPaymentEvent invoiceCreatedEvent) {
        this.paymentId = invoiceCreatedEvent.paymentId;
        this.orderId = invoiceCreatedEvent.orderId;
        this.ammount = invoiceCreatedEvent.ammount;
    }

    @CommandHandler
    public void on(RollbackPaymentCommand rollbackInvoiceCommand) {
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
