package com.example.demo.pubsub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import jakarta.annotation.PostConstruct;

/**
 * 实例化两个test_redis_pubsub的广播消息监听
 * 
 * @author PC
 *
 */
@Configuration
public class MsgPubSub2 {

	@Autowired
	private RedisMessageListenerContainer redisMessageListenerContainer;

	@PostConstruct
	public void init() {
		MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(this, "receiveMessage");
		messageListenerAdapter.afterPropertiesSet();
		redisMessageListenerContainer.addMessageListener(messageListenerAdapter, new PatternTopic("test_redis_pubsub"));
	}

	public void receiveMessage(String msg) {
		System.err.println("MsgPubSub2收到消息:" + msg);
	}

}
