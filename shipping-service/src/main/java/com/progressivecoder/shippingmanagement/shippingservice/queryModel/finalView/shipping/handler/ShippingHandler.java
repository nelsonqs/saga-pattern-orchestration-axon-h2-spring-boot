package com.progressivecoder.shippingmanagement.shippingservice.queryModel.finalView.shipping.handler;

import com.progressivecoder.shippingmanagement.shippingservice.aggregates.ShippingAggregate;
import com.progressivecoder.shippingmanagement.shippingservice.event.OrderShippedEvent;
import com.progressivecoder.shippingmanagement.shippingservice.event.RejectedShippedEvent;
import com.progressivecoder.shippingmanagement.shippingservice.event.RollbackShippedEvent;
import com.progressivecoder.shippingmanagement.shippingservice.queryModel.finalView.shipping.ShippingEntity;
import com.progressivecoder.shippingmanagement.shippingservice.queryModel.finalView.shipping.repositories.ShippingRepository;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ShippingHandler {
    @Autowired
    private ShippingRepository shippingRepository;

    @Autowired
    @Qualifier("shippingAggregateEventSourcingRepository")
    private EventSourcingRepository<ShippingAggregate> shippingAggregateEventSourcingRepository;

    @EventSourcingHandler
    void on(OrderShippedEvent event){
        persistInvoice(buildQueryInvoice(event));
    }

    @EventSourcingHandler
    void on(RollbackShippedEvent event){
        if (shippingRepository.findById(event.shippingId).isPresent()) {
            ShippingEntity shippingEntity = shippingRepository.findById(event.shippingId).get();
            shippingRepository.delete(shippingEntity);
        }
    }

    @EventSourcingHandler
    void on(RejectedShippedEvent event){
        ShippingEntity shippingEntity =  shippingRepository.findById(event.shippingId).isPresent() ? shippingRepository.findById(event.shippingId).get() : new ShippingEntity();
        shippingEntity.setShippingId(event.shippingId);
        shippingEntity.setOrderId(event.orderId);
        shippingEntity.setPaymentId(event.paymentId);
        shippingEntity.setItem(event.item);
        shippingEntity.setStatus("Rejected");
        shippingRepository.save(shippingEntity);
    }

    private ShippingEntity findExistingOrCreateQueryAccount(String id){
        return shippingRepository.findById(id).isPresent() ? shippingRepository.findById(id).get() : new ShippingEntity();
    }

    private ShippingEntity buildQueryInvoice(OrderShippedEvent event){
        ShippingEntity accountQueryEntity = findExistingOrCreateQueryAccount(event.paymentId);
        accountQueryEntity.setPaymentId(event.paymentId);
        accountQueryEntity.setOrderId(event.orderId);
        accountQueryEntity.setShippingId(event.shippingId);
        accountQueryEntity.setItem(event.item);
        accountQueryEntity.setStatus("CREATED");
        return accountQueryEntity;
    }
    private void persistInvoice(ShippingEntity accountQueryEntity){
        shippingRepository.save(accountQueryEntity);
    }
}
