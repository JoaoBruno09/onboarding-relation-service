package com.bank.onboarding.relationservice;

import com.bank.onboarding.commonslib.persistence.models.Relation;
import com.bank.onboarding.commonslib.persistence.repositories.RelationRepository;
import com.bank.onboarding.commonslib.web.SecurityConfig;
import com.bank.onboarding.relationservice.services.RelationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static com.bank.onboarding.commonslib.utils.TestOnboardingUtils.buildRelation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RelationApiIntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private RelationService relationService;

    @Autowired
    private SecurityConfig securityConfig;

    @Value("${bank.onboarding.client.id}")
    private String clientId;

    private String token;

    private HttpHeaders httpHeaders;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        token = securityConfig.generateJWToken();
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBearerAuth(token);
        httpHeaders.set("X-Onboarding-Client-Id", clientId);
        objectMapper.registerModule(new JavaTimeModule());
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/relation/";
    }

    private String insertRelationDB() {
        Relation relaionSaved = relationRepository.save(buildRelation("C123456789"));
        return relaionSaved.getId();
    }

    @Test
    void deleteRelationTest(){
        String relationId = insertRelationDB();

        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<?> response = restTemplate.exchange(
                createURLWithPort() + relationId, HttpMethod.DELETE, entity, new ParameterizedTypeReference<>(){});

        assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));
        assertTrue(relationRepository.findById(relationId).isEmpty());
    }

}
