package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class BeanConfig {

	/**
	 * 创建连接工厂
	 * 
	 * @return
	 */
	@Bean
	public RedisMessageListenerContainer RedisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
		redisMessageListenerContainer.setConnectionFactory(connectionFactory);
		return redisMessageListenerContainer;
	}

}
