package com.bank.onboarding.relationservice.controller;

import com.bank.onboarding.commonslib.persistence.exceptions.OnboardingException;
import com.bank.onboarding.commonslib.utils.OnboardingUtils;
import com.bank.onboarding.relationservice.services.RelationService;
import feign.Request;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("relations")
@AllArgsConstructor
public class RelationController {

    private final RelationService relationService;
    private final OnboardingUtils onboardingUtils;

    @DeleteMapping("/{relationId}")
    public ResponseEntity<?> deleteRelation(@PathVariable("relationId") String relationId){
        try {
            relationService.deleteRelation(relationId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch(OnboardingException e ) {
            return onboardingUtils.buildResponseEntity(Request.HttpMethod.DELETE.name(), e.getMessage());
        }
    }
}
