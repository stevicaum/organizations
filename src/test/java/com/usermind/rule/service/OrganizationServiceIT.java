package com.usermind.rule.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.usermind.rule.ActiveProfileResolverConf;
import com.usermind.rule.controller.organization.OrganizationView;
import com.usermind.rule.controller.response.PageResult;
import com.usermind.rule.repository.customerstatus.CustomerStatus;
import com.usermind.rule.repository.customerstatus.CustomerStatusRepository;
import com.usermind.rule.repository.organization.Organization;
import com.usermind.rule.repository.organization.OrganizationRepository;
import com.usermind.rule.service.kafka.KafkaProducer;
import com.usermind.rule.service.organization.OrganizationService;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles(profiles = {ActiveProfileResolverConf.H_2, ActiveProfileResolverConf.POSTGRES}, resolver =
    ActiveProfileResolverConf.class)
public class OrganizationServiceIT {

  @Autowired
  private OrganizationRepository organizationRepository;
  @Autowired
  private CustomerStatusRepository customerStatusRepository;
  private OrganizationService organizationService;
  @Autowired
  private KafkaProducer kafkaProducer;

  @BeforeEach
  void init() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    organizationService = new OrganizationService(organizationRepository, customerStatusRepository, mapper, kafkaProducer);
  }

  @Test
  @Deprecated
  void listOrganizationsTest() {
    //create organization in db
    assertEquals(0, organizationRepository.count());
    customerStatusRepository.save(createCustomerStatus());
    OrganizationView organizationView = createOrganizationView();
    //create
    organizationView = organizationService.create(organizationView);
    //get from db and check values
    List<OrganizationView> list = organizationService.listOrganizations();
    Optional<Organization> organization = organizationRepository.findByName("name");
    //assert
    assertEquals(1, organizationRepository.count());
    assertFalse(list.isEmpty());
    assertTrue(organization.isPresent());
    assertEquals("name", organization.get().getName());
    assertEquals(organizationView.getId(), String.valueOf(organization.get().getId()));
    assertEquals(organizationView.getName(), organization.get().getName());
    assertEquals(organizationView.getCustomerStatus(), organization.get().getCustomerStatus().getCustomerStatusName());
    assertEquals(organizationView.getCustomer(),organization.get().getCustomer());
    assertEquals(organizationView.getProcessingEnabled(), !organization.get().getProcessingDisabled());
    assertEquals(organizationView.getUmOwner(), organization.get().getUmOwner());
    assertEquals(organizationView.getCustomerContact(), organization.get().getCustomerContact());
    assertEquals(organizationView.getVersion(), organization.get().getVersion());
    assertThat(organizationView.getCreatedAt()).isEqualToIgnoringNanos(organization.get().getCreatedAt());
    assertThat(organizationView.getUpdatedAt()).isEqualToIgnoringNanos(organization.get().getUpdatedAt());
  }

  @Test
//  @Sql("/tests/getOrganizationsTest.sql")
  void getOrganizationsTest() {
    assertEquals(0, organizationRepository.count());
    PageResult<OrganizationView> pageResult = organizationService.getOrganizations(0, 10);
    assertEquals(0, pageResult.getTotalPages());
    assertEquals(0, pageResult.getItems().size());
  }


  private CustomerStatus createCustomerStatus() {
    return new CustomerStatus(1L, "status");
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
