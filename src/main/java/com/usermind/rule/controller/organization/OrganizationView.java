package com.usermind.rule.controller.organization;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrganizationView {
  public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private String id;
  @NotBlank(message = "Name field cant`t be empty")
  @Schema(example = "organization name")
  private String name;
  @NotBlank(message = "Customer status cant`t be empty")
  @Schema(example = "active")
  private String customerStatus;
  @Schema(example = "true", type = "boolean", format = "boolean")
  private Boolean customer;
  private Boolean processingEnabled;
  @Schema(example = "canary")
  private String umOwner;
  @Schema(example = "Address")
  private String customerContact;
  @Positive
  @Schema(example = "1")
  private Long version;
  @Schema(example = "2020-11-30T13:05:28.156Z")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_TIME_FORMAT)
  private LocalDateTime createdAt;
  @Schema(example = "2020-11-30T13:05:28.156Z")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ISO_DATE_TIME_FORMAT)
  private LocalDateTime updatedAt;
  private Map<String, Object> configurations;
}
