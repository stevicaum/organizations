package com.usermind.rule.repository.customerstatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Table(name = "customer_status")
public class CustomerStatus {

  @Id
  @SequenceGenerator(name = "customer_status_customer_status_id_seq", sequenceName = "customer_status_customer_status_id_seq", allocationSize = 1)
  @GeneratedValue(generator = "customer_status_customer_status_id_seq")
  @Column(name = "customer_status_id")
  private Long customerStatusId;
  @Column(name = "customer_status_name")
  private String customerStatusName;

}
