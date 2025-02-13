package com.otus.dialog.configurations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig



@Configuration
class RedisConfig {
  @Bean
  fun jedisPool(): JedisPool {
    val jedisPoolConfig = JedisPoolConfig()
    jedisPoolConfig.jmxEnabled = false
    jedisPoolConfig.maxTotal = 32

    return JedisPool(jedisPoolConfig)
  }
}