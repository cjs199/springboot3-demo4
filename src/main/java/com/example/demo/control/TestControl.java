package com.example.demo.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.jpa.SysUser;
import com.example.demo.jpa.SysUserRepository;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;

@RestController
public class TestControl {

	@Autowired
	private SysUserRepository sysUserRepository;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	// 数据库保存
	@GetMapping("/test_save")
	public void test_save() throws Exception {
		System.err.println("save");
		SysUser sysUser = new SysUser();
		sysUser.setUsername(RandomUtil.randomString(3));
		sysUser.setPassword(RandomUtil.randomString(3));
		sysUserRepository.save(sysUser);
	}

	// 数据库所有查找
	@GetMapping("/test_find_all")
	public String test_find_all() throws Exception {
		List<SysUser> findAll = sysUserRepository.findAll();
		System.err.println(JSONUtil.toJsonPrettyStr(findAll));
		return JSONUtil.toJsonPrettyStr(findAll);
	}

	// 数据库id查找
	@GetMapping("/test_find_by_id")
	public String test_find_by_id(String id) throws Exception {
		SysUser sysUser = sysUserRepository.getOne(id);
		System.err.println(JSONUtil.toJsonPrettyStr(sysUser));
		return JSONUtil.toJsonPrettyStr(sysUser);
	}

	// redis 键值对数据设置
	@GetMapping("/test_redis_kv_set")
	public String test_redis_kv_set() throws Exception {
		stringRedisTemplate.opsForValue().set("123", "456");
		return "OK";
	}

	// redis 键值对数据获取
	@GetMapping("/test_redis_kv_get")
	public String test_redis_kv_get() throws Exception {
		System.err.println(stringRedisTemplate.opsForValue().get("123"));
		return "OK";
	}

	// redis广播消息测试
	@GetMapping("/test_redis_pubsub")
	public String test_redis_pubsub() throws Exception {
		stringRedisTemplate.convertAndSend("test_redis_pubsub", "a msg");
		return "OK";
	}

	// redis队列测试
	@GetMapping("/test_redis_queue")
	public String test_redis_queue() throws Exception {
		stringRedisTemplate.opsForList().rightPush("MsgQueue", "a msg");
		return "OK";
	}


}
