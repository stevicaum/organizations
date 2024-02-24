package com.usermind.rule.controller.organization;

import com.usermind.rule.controller.response.PageResult;
import com.usermind.rule.service.organization.OrganizationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(OrganizationController.ORGANIZATIONS)
@Tag(name = OrganizationController.ORGANIZATIONS)
@Validated
public class OrganizationController {

  public static final String ORGANIZATIONS = "/organizations";
  public static final String PAGE = "page";
  public static final String SIZE = "size";
  public static final String V_2 = "v2";
  public static final String ORGANIZATION_ID = "organizationId";
  public static final String PAGE_SHOULD_BE_GREATER_THEN_0 = "not valid, should be greater then 0";
  public static final String CAPACITY = "capacity";
  private final OrganizationService organizationService;

  public OrganizationController(final OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  /**
   * List all organizations
   *
   * @deprecated This method is no longer acceptable to compute time between versions.
   * <p> Use {@link #getOrganizations(int, int) getOrganizations} instead.
   */
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @Deprecated
  @PreAuthorize("hasRole('CLIENT') || hasAuthority('READ_ORGANIZATIONS')")
  public List<OrganizationView> listOrganizations() {
    return organizationService.listOrganizations();
  }

  @GetMapping(value = V_2, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('CLIENT') || hasAuthority('READ_ORGANIZATIONS')")
  public PageResult<OrganizationView> getOrganizations(@PositiveOrZero(message = PAGE_SHOULD_BE_GREATER_THEN_0) @RequestParam(PAGE) final int page,
                                                       @Positive @RequestParam(SIZE) final int size) {
    return organizationService.getOrganizations(page, size);
  }


  @GetMapping(value = "{" + ORGANIZATION_ID + "}")
  @PreAuthorize("hasRole('CLIENT') || hasAuthority('READ_ORGANIZATIONS')")
  public ResponseEntity<OrganizationView> getOrganization(@PathVariable(ORGANIZATION_ID) @Positive long organizationId) {
    return organizationService.getOrganization(organizationId).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }


  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('CLIENT') || hasAuthority('WRITE_ORGANIZATIONS')")
  public OrganizationView create(@Valid @RequestBody OrganizationView organization) {
    return organizationService.create(organization);
  }


  @PutMapping(value = "{" + ORGANIZATION_ID + "}", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
      MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('CLIENT') || hasAuthority('WRITE_ORGANIZATIONS')")
  public OrganizationView update(@Valid @RequestBody OrganizationView organization,
                                 @PathVariable(ORGANIZATION_ID) @Positive long organizationId) {
    return organizationService.update(organizationId, organization);
  }


  @PutMapping(value = "{" + ORGANIZATION_ID + "}/capping/{" + CAPACITY + "}",
      produces =
      MediaType.APPLICATION_JSON_VALUE)
  public void capping(@PathVariable(ORGANIZATION_ID) Long organizationId,
                      @PathVariable(CAPACITY) Long capacity) {
    organizationService.provisionCapping(organizationId, capacity);
  }
}
