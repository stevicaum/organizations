package com.usermind.rule.repository.organization;

import com.usermind.rule.repository.customerstatus.CustomerStatus;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "`Organizations`")
//@Table(name = "organizations")
@Builder
@TypeDef(name = "json", typeClass = JsonStringType.class)
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
  @Type(type = "json")
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
