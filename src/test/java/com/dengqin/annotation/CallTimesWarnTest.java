package com.dengqin.annotation;

import com.dengqin.annotation.definition.CallTimesWarn;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/5/23.
 */
@Component
public class CallTimesWarnTest {

	@CallTimesWarn(name = "测试警报次数", warnTimes = 5, logLevel = "error")
	public void test() {
		System.out.println("aaaaaaaaaaaaaaaaaaa------ CallTimesWarnTest");
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		final CallTimesWarnTest test = context.getBean(CallTimesWarnTest.class);

//		for (int index = 0; index < 5; index++) {
//			new Thread() {
//				@Override
//				public void run() {
//					try {
//						test.test();
//					} catch (Exception e) {
//						System.out.println(e.getMessage());
//					}
//				}
//			}.start();
//		}
        for (int i = 1; i <= 12 ;i++ ){
            test.test();
        }

	}
}
