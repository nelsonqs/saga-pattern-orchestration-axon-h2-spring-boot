package com.progressivecoder.paymentmanagement.paymentservice.queryModel.finalView.payment.handler;

import com.progressivecoder.paymentmanagement.paymentservice.aggregates.InvoiceAggregate;
import com.progressivecoder.paymentmanagement.paymentservice.events.PaymentCreatedEvent;
import com.progressivecoder.paymentmanagement.paymentservice.events.RejectedPaymentEvent;
import com.progressivecoder.paymentmanagement.paymentservice.events.RollbackPaymentEvent;
import com.progressivecoder.paymentmanagement.paymentservice.queryModel.finalView.payment.InvoiceEntity;
import com.progressivecoder.paymentmanagement.paymentservice.queryModel.finalView.payment.repositories.InvoiceRepository;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class InvoiceHandler {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    @Qualifier("invoiceAggregateEventSourcingRepository")
    private EventSourcingRepository<InvoiceAggregate> invoiceAggregateEventSourcingRepository;

    @EventSourcingHandler
    void on(PaymentCreatedEvent event){
        persistInvoice(buildQueryInvoice(event));
    }

    @EventSourcingHandler
    void on(RejectedPaymentEvent event1) {
        InvoiceEntity invoiceEntity =  invoiceRepository.findById(event1.paymentId).isPresent() ? invoiceRepository.findById(event1.paymentId).get() : new InvoiceEntity();
        invoiceEntity.setInvoiceStatus("REJECTED");
        invoiceRepository.save(invoiceEntity);
    }


    @EventSourcingHandler
    void on(RollbackPaymentEvent rollbackInvoiceEvent) {
        InvoiceEntity invoiceEntity =  invoiceRepository.findById(rollbackInvoiceEvent.paymentId).isPresent() ? invoiceRepository.findById(rollbackInvoiceEvent.paymentId).get() : new InvoiceEntity();
        invoiceRepository.delete(invoiceEntity);
    }

    private InvoiceEntity findExistingOrCreateQueryAccount(String id){
        return invoiceRepository.findById(id).isPresent() ? invoiceRepository.findById(id).get() : new InvoiceEntity();
    }

    private InvoiceEntity buildQueryInvoice(PaymentCreatedEvent event){
        InvoiceEntity accountQueryEntity = findExistingOrCreateQueryAccount(event.paymentId);
        accountQueryEntity.setPaymentId(event.paymentId);
        accountQueryEntity.setOrderId(event.orderId);
        accountQueryEntity.setInvoiceStatus("PAID");
        accountQueryEntity.setAmmount(event.ammount);
        return accountQueryEntity;
    }
    private void persistInvoice(InvoiceEntity accountQueryEntity){
        invoiceRepository.save(accountQueryEntity);
    }
}
