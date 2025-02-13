package com.otus.dialog.configurations

import com.otus.dialog.domain.Message
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer


@Configuration
class RedisConfig {
  @Bean
  fun connectionFactory(): LettuceConnectionFactory {
    return LettuceConnectionFactory()
  }

  @Bean
  fun redisTemplate(connectionFactory: RedisConnectionFactory?): RedisTemplate<String, Message> {
    val template = RedisTemplate<String, Message>()
    template.setDefaultSerializer(RedisSerializer.json())
    template.connectionFactory = connectionFactory

    return template
  }
}