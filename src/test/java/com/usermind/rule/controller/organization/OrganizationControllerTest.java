package com.usermind.rule.controller.organization;

import static com.usermind.rule.controller.organization.OrganizationController.ORGANIZATIONS;
import static com.usermind.rule.controller.organization.OrganizationController.V_2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.usermind.rule.controller.response.PageResult;
import com.usermind.rule.exception.ControllerExceptionHandler;
import com.usermind.rule.service.organization.OrganizationService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class OrganizationControllerTest {
  public static final TypeReference<List<OrganizationView>> LIST_TYPE_REFERENCE = new TypeReference<List<OrganizationView>>() {
  };
  public static final TypeReference<PageResult<OrganizationView>> PAGE_REF = new TypeReference<PageResult<OrganizationView>>() {
  };
  @Mock
  private OrganizationService organizationService;
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void init() {
    objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new JavaTimeModule());
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
    MockitoAnnotations.openMocks(this);
    mockMvc = standaloneSetup(new OrganizationController(organizationService))
        .setMessageConverters(converter).setControllerAdvice(new ControllerExceptionHandler()).build();
  }

  @Test
  @SuppressWarnings("deprecation")
  void listOrganizationsTest() throws Exception {
    final OrganizationView organizationView = createOrganizationView();
    final List<OrganizationView> expectedList = Collections.singletonList(organizationView);
    when(organizationService.listOrganizations()).thenReturn(expectedList);
    final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(ORGANIZATIONS)).andExpect(status().isOk()).andReturn();
    List<OrganizationView> actualList = objectMapper.readValue(result.getResponse().getContentAsByteArray(), LIST_TYPE_REFERENCE);

    assertEquals(expectedList.get(0).getId(), actualList.get(0).getId());
    assertEquals(expectedList.get(0).getName(), actualList.get(0).getName());
    assertEquals(expectedList.get(0).getCustomerStatus(), actualList.get(0).getCustomerStatus());
    assertEquals(expectedList.get(0).getCustomer(), actualList.get(0).getCustomer());
    assertEquals(expectedList.get(0).getProcessingEnabled(), actualList.get(0).getProcessingEnabled());
    assertEquals(expectedList.get(0).getUmOwner(), actualList.get(0).getUmOwner());
    assertEquals(expectedList.get(0).getCustomerContact(), actualList.get(0).getCustomerContact());
    assertEquals(expectedList.get(0).getVersion(), actualList.get(0).getVersion());
    assertThat(expectedList.get(0).getCreatedAt()).isEqualToIgnoringNanos(actualList.get(0).getCreatedAt());
    assertThat(expectedList.get(0).getUpdatedAt()).isEqualToIgnoringNanos(actualList.get(0).getUpdatedAt());
    MapDifference<String, Object> diff = Maps.difference(expectedList.get(0).getConfigurations(),
        actualList.get(0).getConfigurations());
    assertTrue(diff.areEqual());
  }

  @Test
  void getOrganizationsTest() throws Exception {
    final OrganizationView organizationView = createOrganizationView();
    final PageResult<OrganizationView> expectedResult = new PageResult<OrganizationView>(1,
        Collections.singletonList(organizationView));
    final int page = 0;
    final int size = 1;
    when(organizationService.getOrganizations(page, size)).thenReturn(expectedResult);
    final MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(ORGANIZATIONS + "/" + V_2)
        .param(OrganizationController.PAGE, String.valueOf(page))
        .param(OrganizationController.SIZE, String.valueOf(size))).andExpect(status().isOk()).andReturn();
    final PageResult<OrganizationView> actualResult = objectMapper.readValue(result.getResponse().getContentAsByteArray(),
        PAGE_REF);
    assertEquals(expectedResult.getTotalPages(), actualResult.getTotalPages());
    assertEquals(expectedResult.getItems().get(0).getId(), actualResult.getItems().get(0).getId());
    assertEquals(expectedResult.getItems().get(0).getName(), actualResult.getItems().get(0).getName());
    assertEquals(expectedResult.getItems().get(0).getCustomerStatus(), actualResult.getItems().get(0).getCustomerStatus());
    assertEquals(expectedResult.getItems().get(0).getCustomer(), actualResult.getItems().get(0).getCustomer());
    assertEquals(expectedResult.getItems().get(0).getProcessingEnabled(), actualResult.getItems().get(0).getProcessingEnabled());
    assertEquals(expectedResult.getItems().get(0).getUmOwner(), actualResult.getItems().get(0).getUmOwner());
    assertEquals(expectedResult.getItems().get(0).getCustomerContact(), actualResult.getItems().get(0).getCustomerContact());
    assertEquals(expectedResult.getItems().get(0).getVersion(), actualResult.getItems().get(0).getVersion());
    assertThat(expectedResult.getItems().get(0).getCreatedAt()).isEqualToIgnoringNanos(actualResult.getItems().get(0).getCreatedAt());
    assertThat(expectedResult.getItems().get(0).getUpdatedAt()).isEqualToIgnoringNanos(actualResult.getItems().get(0).getUpdatedAt());
    MapDifference<String, Object> diff = Maps.difference(expectedResult.getItems().get(0).getConfigurations(),
        actualResult.getItems().get(0).getConfigurations());
    assertTrue(diff.areEqual());
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
