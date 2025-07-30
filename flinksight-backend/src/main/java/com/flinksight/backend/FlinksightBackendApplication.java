package com.flinksight.backend;

// flinksight-backend（Spring Boot 后端）

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Flinksight 后台API服务启动类
*/
@SpringBootApplication
public class FlinksightBackendApplication {
 public static void main(String[] args) {
     SpringApplication.run(FlinksightBackendApplication.class, args);
 }
}