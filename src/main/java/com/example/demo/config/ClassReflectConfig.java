package com.example.demo.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.ClassUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 反射将所有项目类扫描加入到服务, 大力出奇迹的操作,并不合适,只是为了研究使用服务
 * 
 * @author PC
 *
 */
@Slf4j
@Component
public class ClassReflectConfig {

	static boolean begin = true;

	@Value("${scanclass}")
	private Boolean scanclass;

	@Autowired
	private ThreadPoolTaskExecutor executorService;

	@PostConstruct
	public void init() {

		if (scanclass) {
			log.info("开启了生成反射类");
		} else {
			log.info("关闭了生成反射类");
		}

		synchronized (ClassReflectConfig.class) {
			if (begin && scanclass) {
				begin = false;
				executorService.submit(() -> {

//					{
//						// 先抓取上一次的文件,生成
//						try {
//							BufferedReader utf8Reader = ResourceUtil
//									.getUtf8Reader("classpath:/META-INF/native-image/reflect-config.json");
//							String res = utf8Reader.lines().collect(Collectors.joining());
//							List object = ProJsonUtil.toObject(res, List.class);
//							for (Object object2 : object) {
//								try {
//									Map object22 = (Map) object2;
//									handlerClass(Class.forName(ProMapUtil.getStr(object22, "name")));
//								} catch (Exception e) {
//								}
//							}
//						} catch (Exception e) {
//							log.error("生成文件异常", e);
//						}
//					}

					{
						// 扫描系统第二级开始的包
						String packageName = ClassReflectConfig.class.getPackageName();
						String proPackageName = packageName.substring(0,
								packageName.indexOf(".", packageName.indexOf(".") + 1));

						List<String> asList = Arrays.asList(proPackageName);

						for (String spn : asList) {
							try {
								Set<Class<?>> doScan = ClassUtil.scanPackage(spn);
								for (Class clazz : doScan) {
									handlerClass(clazz);
								}
							} catch (Throwable e) {
								e.printStackTrace();
							}
						}
					}
					
					// handlerClass(RedisMessageListenerContainer.class);

				});
			}
		}


	}

	private void handlerClass(Class clazz) {
		if (clazz.equals(ClassReflectConfig.class)) {
			// 跳过自己,避免形成循环
			return;
		}

		executorService.submit(() -> {
			try {
				log.info("反射注入:{}", clazz.getName());
				// 生成所有的构造器
				Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
				// 找到无参构造器然后实例化
				Constructor declaredConstructor = clazz.getDeclaredConstructor();
				declaredConstructor.setAccessible(true);
				Object newInstance = declaredConstructor.newInstance();
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					try {
						// 实例化成功,那么调用一下
						method.setAccessible(true);
						// graalvm必须需要声明方法
						method.invoke(newInstance);
					} catch (Throwable e) {
					}
				}
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					try {
						field.setAccessible(true);
						field.getType();
						String name = field.getName();
						field.get(newInstance);

					} catch (Throwable e) {
					}
				}
				log.info("反射注入完成:{}", clazz.getName());
			} catch (Throwable e) {
			}
		});
	}

}
