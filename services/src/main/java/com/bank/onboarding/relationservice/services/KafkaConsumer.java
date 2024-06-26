package com.bank.onboarding.relationservice.services;

import com.bank.onboarding.commonslib.persistence.services.CustomerRefRepoService;
import com.bank.onboarding.commonslib.utils.kafka.EventSeDeserializer;
import com.bank.onboarding.commonslib.utils.kafka.models.CreateRelationEvent;
import com.bank.onboarding.commonslib.utils.mappers.CustomerMapper;
import com.bank.onboarding.commonslib.web.dtos.customer.CustomerRefDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.backoff.FixedBackOff;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final EventSeDeserializer eventSeDeserializer;
    private final CustomerRefRepoService customerRefRepoService;
    private final RelationService relationService;

    @KafkaListener(topics = "${spring.kafka.consumer.topic-name}",  groupId = "${spring.kafka.consumer.group-id}")
    public void consumeEvent(ConsumerRecord event){
        String eventValue = event.value().toString();
        String eventKey = event.key().toString();
        switch (eventKey) {
            case "UPDATE_CUSTOMER_REF" -> {
                CustomerRefDTO customerRefDTO = (CustomerRefDTO) eventSeDeserializer.deserialize(eventValue, CustomerRefDTO.class);
                log.info("Event received to update Customer Ref with number {}", customerRefDTO.getCustomerNumber());
                customerRefRepoService.saveCustomerRefDB(CustomerMapper.INSTANCE.toCustomerRef(customerRefDTO));
            }
            case "ADD_REL" -> {
                CreateRelationEvent createRelationEvent = (CreateRelationEvent) eventSeDeserializer.deserialize(eventValue, CreateRelationEvent.class);
                log.info("Event received to create relation for customer with number {}", createRelationEvent.getCustomerRefDTO().getCustomerNumber());
                relationService.addCustomerRelation(createRelationEvent);
            }
        }
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        return new DefaultErrorHandler(new FixedBackOff(0, 0));
    }
}
