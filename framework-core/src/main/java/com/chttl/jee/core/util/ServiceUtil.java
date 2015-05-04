package com.chttl.jee.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServiceUtil {

	private static ApplicationContext ctx = new ClassPathXmlApplicationContext("application.xml") ;
	
	public static <T> T getService(Class<T> clazz){
		return (T) ctx.getBean(clazz);
	}
	
	public static Object getService(String beanId){
		return ctx.getBean(beanId);
	}
	
	//shutdown all context
	public static void shutdownService(){
		ConfigurableApplicationContext c = (ConfigurableApplicationContext) ctx ;
		c.close();
	}

	
}