package com.progressivecoder.shippingmanagement.shippingservice.config;

import com.progressivecoder.shippingmanagement.shippingservice.aggregates.ShippingAggregate;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    EventSourcingRepository<ShippingAggregate> shippingAggregateEventSourcingRepository(EventStore eventStore){
        EventSourcingRepository<ShippingAggregate> repository = EventSourcingRepository.builder(ShippingAggregate.class).eventStore(eventStore).build();
        return repository;
    }
}
