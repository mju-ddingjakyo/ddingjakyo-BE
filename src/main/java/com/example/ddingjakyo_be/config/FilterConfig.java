package com.example.ddingjakyo_be.config;

import com.example.ddingjakyo_be.filter.CorsFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean corsFilter() {
    FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
    filterRegistrationBean.setFilter(new CorsFilter());
    filterRegistrationBean.setOrder(1);  // 첫번째 순서로 넣어주기
    return filterRegistrationBean;
  }
}
