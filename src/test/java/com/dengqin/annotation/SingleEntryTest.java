package com.dengqin.annotation;

import com.dengqin.annotation.definition.SingleEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/5/22.
 * 
 * 单入注解
 */
@Component
public class SingleEntryTest {

	private static Logger logger = LoggerFactory.getLogger(MethodTimeOutTest.class);

	@SingleEntry(entry = "${0}${2}", maxLockSecond = 60)
	public String test(String str, String as, String aa) {
		return "哈哈哈 test for SingleEntryTest ............................." + str;
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		SingleEntryTest methodTimeOutTest = context.getBean(SingleEntryTest.class);
		logger.info(methodTimeOutTest.test("你好！", "喝咯", "交换机"));
	}
}
