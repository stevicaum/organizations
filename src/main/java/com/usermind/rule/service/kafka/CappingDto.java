package com.usermind.rule.service.kafka;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CappingDto {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public Long orgId;
  public Operation operation;
  public Long actionsPerMonth;
}
