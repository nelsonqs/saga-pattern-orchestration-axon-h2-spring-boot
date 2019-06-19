package com.progressivecoder.ordermanagement.orderservice.aggregates;

import com.progressivecoder.ordermanagement.orderservice.commands.CreatePaymentCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.RejectedPaymentCommand;
import com.progressivecoder.ordermanagement.orderservice.commands.RollbackPaymentCommand;
import com.progressivecoder.ordermanagement.orderservice.events.PaymentCreatedEvent;
import com.progressivecoder.ordermanagement.orderservice.events.RejectedPaymentEvent;
import com.progressivecoder.ordermanagement.orderservice.events.RollbackPaymentEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class PaymentAggregate {

    @AggregateIdentifier
    private String paymentId;

    private String orderId;

    private String item;

    private String ammount;

    private InvoiceStatus invoiceStatus;

    private String rejected;

    private String rollback;

    public PaymentAggregate() {
    }

    @CommandHandler
    public PaymentAggregate(CreatePaymentCommand createPaymentCommand){
        AggregateLifecycle.apply(new PaymentCreatedEvent(createPaymentCommand.paymentId, createPaymentCommand.orderId, createPaymentCommand.item, createPaymentCommand.ammount, createPaymentCommand.rejected, createPaymentCommand.rollback));
    }

    @EventSourcingHandler
    protected void on(PaymentCreatedEvent paymentCreatedEvent){
        this.paymentId = paymentCreatedEvent.paymentId;
        this.orderId = paymentCreatedEvent.orderId;
        this.invoiceStatus = InvoiceStatus.PAID;
        this.item = paymentCreatedEvent.item;
        this.ammount = paymentCreatedEvent.ammount;
        this.rejected = paymentCreatedEvent.rejected;
        this.rollback = paymentCreatedEvent.rollback;
    }

    @CommandHandler
    protected void on(RejectedPaymentCommand rejectedPaymentCommand) {
        AggregateLifecycle.apply(new RejectedPaymentEvent(rejectedPaymentCommand.paymentId, rejectedPaymentCommand.orderId, rejectedPaymentCommand.item, rejectedPaymentCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(RejectedPaymentEvent rejectedPaymentEvent){
        this.paymentId = rejectedPaymentEvent.paymentId;
        this.orderId = rejectedPaymentEvent.orderId;
        this.item = rejectedPaymentEvent.item;
        this.ammount = rejectedPaymentEvent.ammount;
    }

    @CommandHandler
    protected void on(RollbackPaymentCommand rollbackPaymentCommand){
        AggregateLifecycle.apply(new RollbackPaymentEvent(rollbackPaymentCommand.paymentId, rollbackPaymentCommand.orderId, rollbackPaymentCommand.item, rollbackPaymentCommand.ammount));
    }

    @EventSourcingHandler
    protected void on(RollbackPaymentEvent rollbackPaymentEvent){
        this.paymentId = rollbackPaymentEvent.paymentId;
        this.orderId = rollbackPaymentEvent.orderId;
        this.item = rollbackPaymentEvent.item;
    }
}
