package com.usermind.rule.repository.organization;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends PagingAndSortingRepository<Organization, Long>, CrudRepository<Organization, Long> {

  /**
   * List all organizations
   *
   * @deprecated This method is no longer acceptable
   * <p> Use {@link #getOrganizations(Pageable)} instead.
   */
  @Deprecated
  @Query("select o from Organization o where o.customerStatus is not null and o.id>0 order by o.id")
  List<Organization> listOrganizations();


  @Query("select o from Organization o where o.customerStatus is not null and o.id>0 order by o.id")
  Page<Organization> getOrganizations(Pageable pageable);

  Optional<Organization> findByName(String name);

  @Query("select case when(count(o)>0) then true else false end from Organization o where o.name = :name")
  boolean doesExistForName(@Param("name") String name);
}
