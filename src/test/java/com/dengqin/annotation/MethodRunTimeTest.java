package com.dengqin.annotation;

import com.dengqin.annotation.definition.MethodRunTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/4/27.
 */
@Component
public class MethodRunTimeTest {

	private static Logger logger = LoggerFactory.getLogger(MethodRunTimeTest.class);

	@MethodRunTime(value = "test测试方法耗时")
	public String test() {
		return "哈哈哈 test for RunTime";
	}

	@MethodRunTime(value = "test2测试方法耗时", usedTime = 50)
	public void test2() {
		for (int i = 1; i <= 10; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		logger.info("嘿嘿嘿 test for void");
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		MethodRunTimeTest methodRunTimeTest = context.getBean(MethodRunTimeTest.class);
		logger.info(methodRunTimeTest.test());
		methodRunTimeTest.test2();
	}

}
