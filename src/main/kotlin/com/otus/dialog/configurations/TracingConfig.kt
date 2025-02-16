package com.otus.dialog.configurations

import io.micrometer.tracing.Tracer
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException


@Configuration
class TracingConfig {
  @Bean
  fun tracingFilter(tracer: Tracer): GenericFilterBean {
    return object : GenericFilterBean() {
      @Throws(IOException::class, ServletException::class)
      override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val customHeaderValue = httpRequest.getHeader("X-Request-Id")

        if (customHeaderValue != null) {
          tracer.currentSpan()?.tag("x-request-id", customHeaderValue)
        }

        chain.doFilter(request, response)
      }
    }
  }
}