package com.progressivecoder.ordermanagement.orderservice.config;

import com.progressivecoder.ordermanagement.orderservice.aggregates.OrderAggregate;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    EventSourcingRepository<OrderAggregate> orderAggregateEventSourcingRepository(EventStore eventStore){
        EventSourcingRepository<OrderAggregate> repository = EventSourcingRepository.builder(OrderAggregate.class).eventStore(eventStore).build();
        return repository;
    }
}
