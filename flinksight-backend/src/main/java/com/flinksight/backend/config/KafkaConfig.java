// src/main/java/com/flinksight/backend/config/KafkaConfig.java
package com.flinksight.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

  /** 规则变更事件 Topic 名称（可在 application.yml 配置） */
  @Bean
  public String ruleSetChangeTopic(@Value("${flinksight.kafka.topics.ruleSetChange:flinksight.alert-rules}") String topic) {
    return topic;
  }

  /** （可选）自动创建 topic。生产环境通常由运维独立管理，可关闭 */
  @Bean
  public NewTopic ruleSetChangeNewTopic(String ruleSetChangeTopic,
                                        @Value("${flinksight.kafka.partitions:3}") int partitions,
                                        @Value("${flinksight.kafka.replication:1}") short replication) {
    return new NewTopic(ruleSetChangeTopic, partitions, replication);
  }

}
