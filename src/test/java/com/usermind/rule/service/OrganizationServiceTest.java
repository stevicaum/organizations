package com.usermind.rule.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.usermind.rule.controller.organization.OrganizationView;
import com.usermind.rule.repository.customerstatus.CustomerStatus;
import com.usermind.rule.repository.customerstatus.CustomerStatusRepository;
import com.usermind.rule.repository.organization.Organization;
import com.usermind.rule.repository.organization.OrganizationRepository;
import com.usermind.rule.service.kafka.KafkaProducer;
import com.usermind.rule.service.organization.OrganizationService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrganizationServiceTest {

  @Mock
  private OrganizationRepository organizationRepository;
  @Mock
  private CustomerStatusRepository customerStatusRepository;
  @Mock
  private KafkaProducer kafkaProducer;
  private OrganizationService organizationService;
  private ObjectMapper mapper;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
    mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    organizationService = new OrganizationService(organizationRepository, customerStatusRepository, mapper, kafkaProducer);
  }

  @Test
  void list(){
    for(String a: listaStringova()){
      System.out.println("for "+a);
    }
  }

  private List<String> listaStringova() {
    System.out.println("svaki put");
    List<String> a = new ArrayList<>();
    a.add("stevica");
    a.add("g");
    return a;
  }

  @Test
  @Deprecated
  void listOrganizationsTest() throws JsonProcessingException {
    Organization organization = createOrganization();
    when(organizationRepository.listOrganizations()).thenReturn(Collections.singletonList(organization));
    List<OrganizationView> list = organizationService.listOrganizations();
    assertFalse(list.isEmpty());
    assertEquals(1, list.size());

    assertEquals(String.valueOf(organization.getId()),list.get(0).getId());
  }


  private CustomerStatus createCustomerStatus() {
    return new CustomerStatus(1L, "status");
  }

  private Organization createOrganization() throws JsonProcessingException {
    HashMap<String, Object> configurations = new HashMap<>();
    configurations.put("key", "3");
    CustomerStatus customerStatus = new CustomerStatus(20L ,"status");
    String configuration = mapper.writeValueAsString(configurations);
    final Organization organizationView = new Organization(10L, "name", true,
        true, false, "umOwner", "customerContact", 1L,
        configuration, customerStatus, LocalDateTime.now(), LocalDateTime.now());
    return organizationView;
  }

  private OrganizationView createOrganizationView() {
    HashMap<String, Object> configurations = new HashMap<>();
    configurations.put("key", "3");
    final OrganizationView organizationView = new OrganizationView("id", "name", "status", true,
        true, "umOwner", "customerContact", 1L, LocalDateTime.now(), LocalDateTime.now(),
        configurations);
    return organizationView;
  }
}
