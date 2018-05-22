package com.dengqin.annotation;

import com.dengqin.annotation.definition.MethodTimeOut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/4/28.
 */
@Component
public class MethodTimeOutTest {

	private static Logger logger = LoggerFactory.getLogger(MethodTimeOutTest.class);

	@MethodTimeOut()
	public String test() {
		return "哈哈哈 test for RunTime";
	}

	@MethodTimeOut()
	public void test2() {
		for (int i = 1; i <= 4; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("嘿嘿嘿 test for void");
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		MethodTimeOutTest methodTimeOutTest = context.getBean(MethodTimeOutTest.class);
		logger.info(methodTimeOutTest.test());
		methodTimeOutTest.test2();
	}

}
