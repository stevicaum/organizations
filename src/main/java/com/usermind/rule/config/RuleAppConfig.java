package com.usermind.rule.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.usermind.rule.config.security.SecurityConfig;
import com.usermind.rule.repository.customerstatus.CustomerStatusRepository;
import com.usermind.rule.repository.organization.OrganizationRepository;
import com.usermind.rule.service.kafka.KafkaProducer;
import com.usermind.rule.service.organization.OrganizationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@Import( {
    SecurityConfig.class
})
public class RuleAppConfig {

  @Bean
  public ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  @Bean
  public KafkaProducer kafkaProducer(final KafkaTemplate<Long, String> kafkaTemplate,
                                     @Value("${events.topic}") final String eventsTopic, final  ObjectMapper objectMapper) {
    return new KafkaProducer(kafkaTemplate, eventsTopic, objectMapper);
  }

  @Bean
  public OrganizationService organizationService(final OrganizationRepository organizationRepository,
                                                 final CustomerStatusRepository customerStatusRepository,
                                                 final ObjectMapper objectMapper, final KafkaProducer kafkaProducer) {
    return new OrganizationService(organizationRepository, customerStatusRepository, objectMapper, kafkaProducer);
  }
}
