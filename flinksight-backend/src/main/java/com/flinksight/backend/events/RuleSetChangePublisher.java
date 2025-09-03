// src/main/java/com/flinksight/backend/events/RuleSetChangePublisher.java
package com.flinksight.backend.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RuleSetChangePublisher {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String ruleSetChangeTopic;
  private final ObjectMapper om;

  public void publish(RuleSetChangeEvent evt) {
    try {
      String key = evt.getTenantId() + ":" + evt.getScopeType() + ":" + String.valueOf(evt.getScopeId());
      String value = om.writeValueAsString(evt);
      kafkaTemplate.send(ruleSetChangeTopic, key, value);
    } catch (Exception e) {
      // 生产建议：告警 & 重试（如 outbox + 定时补偿）；此处不抛出影响主流程
    }
  }
}
