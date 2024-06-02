package com.bank.onboarding.relationservice.services;

import com.bank.onboarding.commonslib.utils.kafka.models.ErrorEvent;

public interface RelationService {
    void handleErrorEvent(ErrorEvent errorEvent);
}
