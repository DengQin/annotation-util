package com.dengqin.annotation;

import com.dengqin.annotation.definition.CacheHitRateWarn;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by dq on 2018/5/24.
 */
@Component
public class CacheHitRateWarnTest {

	boolean isPass = false;

	@CacheHitRateWarn(logLevel = "error", name = "缓存穿透测试", minTimes = 10, minRate = 0.9, isPassCache = false)
	public void test(@PathVariable(value = "ddd") int a) {
		System.out.println("CacheHitRateWarnTest---------是否穿透了。。。。。。。。。" + a);
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		CacheHitRateWarnTest test = context.getBean(CacheHitRateWarnTest.class);
		for (int i = 1; i <= 12; i++) {
			test.test(i);
		}
	}
}
