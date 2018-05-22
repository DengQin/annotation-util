package com.dengqin.annotation;

import com.dengqin.annotation.definition.IPDenied;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dq on 2018/5/10.
 */
@Component
public class IPDeniedTest {

	private static Logger logger = LoggerFactory.getLogger(IPDeniedTest.class);

	@IPDenied(ips = "127.0.0.3,127.0.0.2")
	public void test(HttpServletRequest request) {
		System.out.println("IP限制测试。。。。。。");
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		IPDeniedTest ipDeniedTest = context.getBean(IPDeniedTest.class);

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn("127.0.0.2");
        logger.info("ip为: " + request.getHeader("X-Real-IP"));
		ipDeniedTest.test(request);
	}
}
