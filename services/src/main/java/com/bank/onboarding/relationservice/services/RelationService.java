package com.bank.onboarding.relationservice.services;

import com.bank.onboarding.commonslib.utils.kafka.models.CreateRelationEvent;
import com.bank.onboarding.commonslib.utils.kafka.models.ErrorEvent;

public interface RelationService {
    void addCustomerRelation(CreateRelationEvent createRelationEvent);
    void deleteRelation(String relationId);
}
