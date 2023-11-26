package com.example.ddingjakyo_be.config;

import jakarta.servlet.SessionCookieConfig;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

  @Bean
  public ServletContextInitializer servletContextInitializer() {
    return servletContext -> {
      final SessionCookieConfig sessionCookieConfig = servletContext.getSessionCookieConfig();
      sessionCookieConfig.setHttpOnly(false);
    };
  }
}
