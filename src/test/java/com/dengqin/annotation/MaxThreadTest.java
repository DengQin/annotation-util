package com.dengqin.annotation;

import com.dengqin.annotation.definition.MaxThread;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/5/23.
 */
@Component
public class MaxThreadTest {

	int index = 0;

	@MaxThread(name = "测试连接数", max = "5")
	public void test() {
		System.out.println("hhhhhhhhhhhhhhhh------ 线程：" + Thread.currentThread().getName() + "," + index++);
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		final MaxThreadTest test = context.getBean(MaxThreadTest.class);

		for (int index = 0; index < 8; index++) {
			new Thread() {
				@Override
				public void run() {
					// for (int i = 0; i < 10; i++) {
					try {
						test.test();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						// }
					}
				}
			}.start();
		}
	}

}
