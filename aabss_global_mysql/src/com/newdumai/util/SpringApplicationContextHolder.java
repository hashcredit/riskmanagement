package com.newdumai.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component("springApplicationContextHolder") 
public class SpringApplicationContextHolder implements ApplicationListener<ContextRefreshedEvent> {

	private static ApplicationContext context;

	public static Object getBean(String beanName) {
		if (context != null) {
			return context.getBean(beanName);
		} else
			return null;
	}

	public static <T> T getBean(Class<T> clazz) {
		if (context != null) {
			return context.getBean(clazz);
		} else
			return null;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		context = event.getApplicationContext();
	}
}
