package com.usermind.rule.service.organization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.usermind.rule.controller.organization.OrganizationView;
import com.usermind.rule.controller.response.PageResult;
import com.usermind.rule.exception.AlreadyExistException;
import com.usermind.rule.exception.FieldValidationException;
import com.usermind.rule.exception.ResourceNotFoundException;
import com.usermind.rule.repository.customerstatus.CustomerStatus;
import com.usermind.rule.repository.customerstatus.CustomerStatusRepository;
import com.usermind.rule.repository.organization.Organization;
import com.usermind.rule.repository.organization.OrganizationRepository;
import com.usermind.rule.service.kafka.KafkaProducer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class OrganizationService {

  public static final TypeReference<Map<String, Object>> VALUE_TYPE_REF = new TypeReference<Map<String, Object>>() {
  };
  private final OrganizationRepository organizationRepository;
  private final CustomerStatusRepository customerStatusRepository;
  private final ObjectMapper objectMapper;
  private final KafkaProducer kafkaProducer;

  public OrganizationService(final OrganizationRepository organizationRepository,
                             final CustomerStatusRepository customerStatusRepository,
                             final ObjectMapper objectMapper, KafkaProducer kafkaProducer) {
    this.organizationRepository = organizationRepository;
    this.customerStatusRepository = customerStatusRepository;
    this.objectMapper = objectMapper;
    this.kafkaProducer = kafkaProducer;
  }

  /**
   * List all organizations
   *
   * @deprecated This method is no longer acceptable to compute time between versions.
   * <p> Use {@link #getOrganizations(int, int)} instead.
   */
  @Deprecated
  public List<OrganizationView> listOrganizations() {
    List<Organization> organizationList = organizationRepository.listOrganizations();
    return organizationList.stream().map(o -> mapOrganization(o)).collect(Collectors.toList());
  }

  public PageResult<OrganizationView> getOrganizations(int page, int size) {
    final Page<Organization> pageObject = organizationRepository.getOrganizations(PageRequest.of(page, size));
    return new PageResult<>(pageObject.getTotalPages(),
        pageObject.stream().map(o -> mapOrganization(o)).collect(Collectors.toList()));
  }

  public Optional<OrganizationView> getOrganization(long organizationId) {
    return organizationRepository.findById(organizationId).map(o -> mapOrganization(o));
  }


  @Transactional
  public OrganizationView create(final OrganizationView organizationView) {
    if (organizationRepository.doesExistForName(organizationView.getName())) {
      throw new AlreadyExistException("name", organizationView.getName());
    }
    CustomerStatus customerStatus = customerStatusRepository.findByCustomerStatusName(organizationView.getCustomerStatus())
        .orElseThrow(() -> new FieldValidationException("customerStatus"));

    String configuration = null;
    try {
      configuration = objectMapper.writeValueAsString(organizationView.getConfigurations());
    } catch (IOException ex) {
      log.error("Parsing exception for configuration {}", organizationView.getConfigurations(), ex);
    }
    Organization organization = Organization.builder().name(organizationView.getName()).replay(Boolean.FALSE)
        .customerContact(organizationView.getCustomerContact()).configurations(configuration)
        .customer(organizationView.getCustomer()).processingDisabled(!organizationView.getProcessingEnabled())
        .umOwner(organizationView.getUmOwner()).customerContact(organizationView.getCustomerContact())
        .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
        .customerStatus(customerStatus).build();
    return mapOrganization(organizationRepository.save(organization));
  }

  @Transactional
  public OrganizationView update(final Long organizationId, final OrganizationView organizationView) {
    Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new ResourceNotFoundException(
        "Organization with id dont exist, id:" + organizationId));

    CustomerStatus customerStatus = customerStatusRepository.findByCustomerStatusName(organizationView.getCustomerStatus())
        .orElseThrow(() -> new FieldValidationException("customerStatus"));

    String configuration = null;
    try {
      configuration = objectMapper.writeValueAsString(organizationView.getConfigurations());
    } catch (IOException ex) {
      log.error("Parsing exception for configuration {}", organizationView.getConfigurations(), ex);
    }
    organization.setName(organizationView.getName());
    organization.setProcessingDisabled(!organizationView.getProcessingEnabled());
    organization.setCustomer(organizationView.getCustomer());
    organization.setCustomerStatus(customerStatus);
    organization.setUmOwner(organizationView.getUmOwner());
    organization.setCustomerContact(organizationView.getCustomerContact());
    organization.setConfigurations(configuration);
    return mapOrganization(organizationRepository.save(organization));
  }

  public void provisionCapping(Long organizationId, Long capacity) {
    kafkaProducer.sendCreateCapping(organizationId, capacity);
  }

  private OrganizationView mapOrganization(final Organization organization) {
    Map<String, Object> configs = null;
    if (organization.getConfigurations() != null) {
      try {
        configs = objectMapper.readValue(organization.getConfigurations(), VALUE_TYPE_REF);
      } catch (IOException exception) {
        log.warn("Unparsable JSON was returned from the DB for Organization {}", organization.getId(), exception);
      }
    }
    return new OrganizationView(
        String.valueOf(organization.getId()),
        organization.getName(),
        organization.getCustomerStatus().getCustomerStatusName(),
        organization.getCustomer(),
        !organization.getProcessingDisabled(),
        organization.getUmOwner(),
        organization.getCustomerContact(),
        organization.getVersion(),
        organization.getCreatedAt(),
        organization.getUpdatedAt(),
        configs
    );
  }
}
