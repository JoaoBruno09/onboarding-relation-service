package com.bank.onboarding.relationservice.controllers;

import com.bank.onboarding.commonslib.persistence.repositories.AccountRepository;
import com.bank.onboarding.commonslib.persistence.repositories.AddressRepository;
import com.bank.onboarding.commonslib.persistence.repositories.CardRepository;
import com.bank.onboarding.commonslib.persistence.repositories.ContactRepository;
import com.bank.onboarding.commonslib.persistence.repositories.CustomerRefRepository;
import com.bank.onboarding.commonslib.persistence.repositories.CustomerRepository;
import com.bank.onboarding.commonslib.persistence.repositories.DocumentRepository;
import com.bank.onboarding.commonslib.persistence.repositories.InterventionRepository;
import com.bank.onboarding.commonslib.persistence.repositories.RelationRepository;
import com.bank.onboarding.commonslib.persistence.services.AccountRepoService;
import com.bank.onboarding.commonslib.persistence.services.CardRepoService;
import com.bank.onboarding.commonslib.persistence.services.CustomerRefRepoService;
import com.bank.onboarding.commonslib.utils.OnboardingUtils;
import com.bank.onboarding.commonslib.web.SecurityConfig;
import com.bank.onboarding.relationservice.controller.RelationController;
import com.bank.onboarding.relationservice.services.RelationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RelationController.class)
@Import(SecurityConfig.class)
@MockBeans({
        @MockBean(OnboardingUtils.class),
        @MockBean(AccountRepoService.class),
        @MockBean(CardRepoService.class),
        @MockBean(CustomerRefRepoService.class),
        @MockBean(CustomerRefRepository.class),
        @MockBean(CustomerRepository.class),
        @MockBean(AccountRepository.class),
        @MockBean(CardRepository.class),
        @MockBean(DocumentRepository.class),
        @MockBean(InterventionRepository.class),
        @MockBean(RelationRepository.class),
        @MockBean(ContactRepository.class),
        @MockBean(AddressRepository.class),
        @MockBean(RelationService.class)
})
class RelationControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SecurityConfig securityConfig;

    @Value("${bank.onboarding.client.id}")
    private String clientId;

    private String token;


    @BeforeEach
    public void setUp() {
        token = securityConfig.generateJWToken();
    }

    @Test
    void deleteRelationTest() throws Exception{
        mockMvc.perform(delete("/relations/R123456789")
                        .header("Authorization", "Bearer " + token)
                        .header("X-Onboarding-Client-Id", clientId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
