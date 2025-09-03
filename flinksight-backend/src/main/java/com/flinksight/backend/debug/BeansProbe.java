// src/main/java/com/flinksight/backend/debug/BeansProbe.java
package com.flinksight.backend.debug;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BeansProbe implements ApplicationRunner {
  private final ApplicationContext ctx;

  @Override public void run(org.springframework.boot.ApplicationArguments args) {
    // 1) 所有 ObjectMapper Bean
    var oms = ctx.getBeansOfType(ObjectMapper.class);
    oms.forEach((name, om) ->
        log.info("[Probe] OM bean='{}' modules={}", name, om.getRegisteredModuleIds()));

    // 2) 所有 MVC Jackson 转换器 Bean
    var convs = ctx.getBeansOfType(MappingJackson2HttpMessageConverter.class);
    convs.forEach((name, m) ->
        log.info("[Probe] Conv bean='{}' modules={}", name, m.getObjectMapper().getRegisteredModuleIds()));
  }
}
