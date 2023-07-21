package com.example.demo.queue;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import jakarta.annotation.PostConstruct;

/**
 * 实例化两个test_redis_pubsub的广播消息监听
 * 
 * @author PC
 *
 */
@Configuration
public class MsgQueue {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private ThreadPoolTaskExecutor executorService;

	@PostConstruct
	public void init() {

		executorService.execute(() -> {
			while (true) {
				try {
					if (stringRedisTemplate == null) {
						continue;
					}
					// 每次监听等待60s,60s还没有收到新消息时,返回null再次循环等待
					Object listLeftPop = stringRedisTemplate.opsForList().leftPop("MsgQueue", 60, TimeUnit.SECONDS);
					if (listLeftPop != null) {
						System.err.println("收到队列消息:" + listLeftPop);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});

	}


}
