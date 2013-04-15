package com.chenjw.spider.aliuser;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public void doWork() {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:bean/main.xml");
		AliUserService aliUserService = (AliUserService) context
				.getBean("aliUserService");
		aliUserService.start();
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.doWork();
		System.out.println("finished!");
	}
}
