package com.usermind.rule.service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class KafkaProducer {
  private static final String HEADER_SOURCE = "source";
  private static final String HEADER_SOURCE_VALUE = "rules-store";

  private static final String HEADER_OPERATION = "operation";

  private static final String HEADER_TYPE = "type";
  private static final String HEADER_TYPE_CAPPING = "capping";
  private static final String HEADER_TYPE_ORGANIZATION = "organization";
  private final KafkaTemplate<Long, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private String eventsTopic;

  public KafkaProducer(final KafkaTemplate<Long, String> kafkaTemplate, final String eventsTopic, final ObjectMapper objectMapper){
    this.kafkaTemplate = kafkaTemplate;
    this.eventsTopic = eventsTopic;
    this.objectMapper = objectMapper;
  }

  public void sendCreateCapping(Long orgId, Long actionsPerMonth) {
    CappingDto dto = new CappingDto();
    dto.orgId = orgId;
    dto.operation = Operation.CREATE;
    dto.actionsPerMonth = actionsPerMonth;
    send(orgId, dto, createHeaders(dto.operation.toString().toLowerCase(), HEADER_TYPE_CAPPING));
  }

  private List<Header> createHeaders(String operation, String type) {
    List<Header> kafkaHeaders = new ArrayList<>();
    kafkaHeaders.add(new RecordHeader(HEADER_SOURCE, HEADER_SOURCE_VALUE.getBytes(StandardCharsets.UTF_8)));
    kafkaHeaders.add(new RecordHeader(HEADER_OPERATION, operation.getBytes(StandardCharsets.UTF_8)));
    kafkaHeaders.add(new RecordHeader(HEADER_TYPE, type.getBytes(StandardCharsets.UTF_8)));

    return kafkaHeaders;
  }

  private void send(Long key, Object dto, List<Header> headers) {
    String serialized;
    try {
      serialized = objectMapper.writeValueAsString(dto);
    } catch (JsonProcessingException e) {
      log.error("Caught exception while trying to serialize", e);
      return;
    }
    ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(eventsTopic, null, key, serialized, headers);
    kafkaTemplate.send(record);
  }

}
