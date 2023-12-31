package com.example.ddingjakyo_be.config;

import com.example.ddingjakyo_be.interceptor.LogInCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInCheckInterceptor())
            .addPathPatterns("/**") // 모든 URI에서 인터셉터 실행
            .excludePathPatterns("/api/log*", "/api/register", "/api/email*",
                    "/api/email_certification/confirm",
                    "/api/teams",
                    "/api/email_certification/duplicated*"); // login, logout, 회원가입, 이메일 인증, 전체 팀 조회 URI는 인터셉터 실행에서 제외
  }

//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**")
//            .allowedOriginPatterns("*")
//            .allowCredentials(true)
//            .allowedHeaders("*")
//            .allowedMethods("*");
//  }
}
