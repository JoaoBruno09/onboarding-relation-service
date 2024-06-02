package com.bank.onboarding.relationservice.services.impl;

import com.bank.onboarding.commonslib.persistence.services.CustomerRefRepoService;
import com.bank.onboarding.commonslib.utils.kafka.models.ErrorEvent;
import com.bank.onboarding.relationservice.services.RelationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.bank.onboarding.commonslib.persistence.enums.OperationType.ADD_INTERVENIENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class RelationServiceImpl implements RelationService {

    private final CustomerRefRepoService customerRefRepoService;

    @Override
    public void handleErrorEvent(ErrorEvent errorEvent) {
        if (ADD_INTERVENIENT.equals(errorEvent.getOperationType())){
            customerRefRepoService.deleteCustomerById(errorEvent.getCustomerRefDTO().getCustomerId());
        }
    }
}
