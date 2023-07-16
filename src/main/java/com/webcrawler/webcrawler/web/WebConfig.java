package com.webcrawler.webcrawler.web;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.strategy.SegmentNamingStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class WebConfig {
  @Bean
  public AWSXRayServletFilter TracingFilter() {
    return new AWSXRayServletFilter(SegmentNamingStrategy.dynamic("webcrawl"));
  }
}