package com.exercise.springboot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000") // 클라이언트 주소
        .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드
        .allowedHeaders("*"); // 요청 헤더
  }

}
