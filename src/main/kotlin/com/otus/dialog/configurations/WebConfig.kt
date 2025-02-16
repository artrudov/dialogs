package com.otus.dialog.configurations

import io.micrometer.tracing.Tracer
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.GenericFilterBean


@Configuration
class WebConfig {
  @Bean
  fun tracingFilterRegistration(tracer: Tracer): FilterRegistrationBean<GenericFilterBean> {
    val registrationBean = FilterRegistrationBean<GenericFilterBean>()
    registrationBean.filter = TracingConfig().tracingFilter(tracer)
    registrationBean.addUrlPatterns("/*")

    return registrationBean
  }
}