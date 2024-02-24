package com.usermind.rule.repository.organization;

import com.usermind.rule.repository.customerstatus.CustomerStatus;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "`Organizations`")
//@Table(name = "organizations")
@Builder
public class Organization {
  @Id
  @SequenceGenerator(name = "Customers_id_seq", sequenceName = "Customers_id_seq", allocationSize = 1)
  @GeneratedValue(generator = "Customers_id_seq")
  private Long id;
  private String name;
  private Boolean replay;
  private Boolean customer;
  @Column(name = "proc_disabled")
  private Boolean processingDisabled;
  @Column(name = "um_owner")
  private String umOwner;
  @Column(name = "customer_contact")
  private String customerContact;
  @Column(name = "version",nullable = false)
  @Version
  private Long version;
  @Type(JsonType.class)
  @Column(columnDefinition = "json")
  private String configurations;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "customer_status", referencedColumnName = "customer_status_id")
  private CustomerStatus customerStatus;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

}
