package com.bruno.taskflow_api.shared.infrastructure.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

/*  @Bean
  public RedisCacheConfiguration cacheConfiguration() {
    PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
        .allowIfBaseType(Object.class).build();

    JsonMapper jsonMapper = JsonMapper.builder().activateDefaultTyping(ptv, DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY // <-- Ahora compila correctamente
    ).build();

    GenericJacksonJsonRedisSerializer serializer = new GenericJacksonJsonRedisSerializer(
        jsonMapper);

    return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(serializer));
  }*/
}