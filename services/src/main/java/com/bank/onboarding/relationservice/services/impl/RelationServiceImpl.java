package com.bank.onboarding.relationservice.services.impl;

import com.bank.onboarding.commonslib.persistence.exceptions.OnboardingException;
import com.bank.onboarding.commonslib.persistence.models.Relation;
import com.bank.onboarding.commonslib.persistence.services.CustomerRefRepoService;
import com.bank.onboarding.commonslib.persistence.services.RelationRepoService;
import com.bank.onboarding.commonslib.utils.OnboardingUtils;
import com.bank.onboarding.commonslib.utils.kafka.models.CreateRelationEvent;
import com.bank.onboarding.commonslib.utils.kafka.models.ErrorEvent;
import com.bank.onboarding.commonslib.web.dtos.account.AccountRefDTO;
import com.bank.onboarding.commonslib.web.dtos.customer.CreateRelationDTO;
import com.bank.onboarding.commonslib.web.dtos.customer.CustomerRefDTO;
import com.bank.onboarding.commonslib.web.dtos.customer.CustomerRequestDTO;
import com.bank.onboarding.relationservice.services.RelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bank.onboarding.commonslib.persistence.constants.OnboardingConstants.RELATION_TYPES;
import static com.bank.onboarding.commonslib.persistence.enums.OperationType.ADD_INTERVENIENT;
import static com.bank.onboarding.commonslib.persistence.enums.OperationType.ADD_REL;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelationServiceImpl implements RelationService {

    private final CustomerRefRepoService customerRefRepoService;
    private final RelationRepoService relationRepoService;
    private final OnboardingUtils onboardingUtils;

    @Value("${spring.kafka.producer.customer.topic-name}")
    private String customerTopicName;

    @Value("${spring.kafka.producer.account.topic-name}")
    private String accountTopicName;

    @Value("${spring.kafka.producer.document.topic-name}")
    private String documentTopicName;

    @Override
    public void handleErrorEvent(ErrorEvent errorEvent) {
        if (ADD_INTERVENIENT.equals(errorEvent.getOperationType())){
            customerRefRepoService.deleteCustomerById(errorEvent.getCustomerRefDTO().getCustomerId());
        }
    }

    @Override
    public void addCustomerRelation(CreateRelationEvent createRelationEvent) {
        String relationType = Optional.ofNullable(createRelationEvent.getCreateRelationDTO()).map(CreateRelationDTO::getParentCustomer)
                .map(CustomerRequestDTO::getCustomerRelationType).orElse("");

        String parentCustomerId = Optional.ofNullable(createRelationEvent.getCustomerRefDTO()).map(CustomerRefDTO::getCustomerId).orElse("");
        int relationsSize = relationRepoService.getAllRelationsByCustomerId(parentCustomerId).size();

        String childCustomerId = customerRefRepoService.findCustomerRefByCustomerNumber(Optional.ofNullable(createRelationEvent.getCreateRelationDTO())
                .map(CreateRelationDTO::getChildCustomerNumber).orElse("")).getId();

        saveRelation(relationType, parentCustomerId, childCustomerId, createRelationEvent.getAccountRefDTO(),
                createRelationEvent.getCustomerRefDTO(), createRelationEvent.isNewCustomer(), relationsSize);
    }

    private void saveRelation(String relationType, String parentCustomerId, String childIdCustomerId, AccountRefDTO accountRefDTO, CustomerRefDTO customerRefDTO, boolean isNewCustomer, int relationsSize) {
        if(!RELATION_TYPES.contains(relationType)){
            sendEventErrors(accountRefDTO, customerRefDTO, isNewCustomer, relationsSize);
            throw new OnboardingException("O tipo de relação introduzida é inválida");
        }

        relationRepoService.saveRelationDB(Relation.builder()
                .creationTime(LocalDateTime.now())
                .childId(childIdCustomerId)
                .description(onboardingUtils.getRelationTypeValue(relationType))
                .fatherId(parentCustomerId)
                .lastUpdateTime(LocalDateTime.now())
                .relationType(relationType)
                .build());
    }

    private void sendEventErrors(AccountRefDTO accountRefDTO, CustomerRefDTO customerRefDTO, boolean isNewCustomer, int relationsSize) {
        if(Boolean.TRUE.equals(isNewCustomer)){
            onboardingUtils.sendErrorEvent(accountTopicName, accountRefDTO, customerRefDTO, ADD_REL, true);
            onboardingUtils.sendErrorEvent(documentTopicName, accountRefDTO, customerRefDTO, ADD_REL, true);
        }
        if (relationsSize == 0) onboardingUtils.sendErrorEvent(customerTopicName, accountRefDTO, customerRefDTO, ADD_REL, isNewCustomer);
    }
}
