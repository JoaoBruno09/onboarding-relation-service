package com.bank.onboarding.relationservice.controller;

import com.bank.onboarding.commonslib.persistence.models.Relation;
import com.bank.onboarding.commonslib.persistence.services.RelationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/relation")
@AllArgsConstructor
public class RelationController {

    private final RelationService relationService;

    @GetMapping("/test")
    public List<Relation> getInterventions() {
        return relationService.getAllRelations();
    }
}
