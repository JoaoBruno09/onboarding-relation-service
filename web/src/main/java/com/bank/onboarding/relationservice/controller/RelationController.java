package com.bank.onboarding.relationservice.controller;

import com.bank.onboarding.commonslib.persistence.exceptions.OnboardingException;
import com.bank.onboarding.relationservice.services.RelationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relation")
@AllArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @DeleteMapping("/{relationId}")
    public ResponseEntity<?> deleteRelation(@PathVariable("relationId") String relationId){
        try {
            relationService.deleteRelation(relationId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(OnboardingException e ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
