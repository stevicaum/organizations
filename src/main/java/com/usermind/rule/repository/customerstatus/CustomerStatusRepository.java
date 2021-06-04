package com.usermind.rule.repository.customerstatus;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerStatusRepository extends JpaRepository<CustomerStatus, Long> {

  /**
   * This is Spring Derived query by name(@Query not needed).
   *
   * @param customerStatusName String
   * @return instance of CustomerStatus
   */
  Optional<CustomerStatus> findByCustomerStatusName(String customerStatusName);
}
