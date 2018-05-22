package com.dengqin.annotation;

import com.dengqin.annotation.definition.AdminLog;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dq on 2018/5/16.
 */
@Component
public class AdminLogTest {

	private static Logger logger = LoggerFactory.getLogger(IPDeniedTest.class);
	@Autowired(required = false)
	private HttpServletRequest request;

	@AdminLog(content = "test for 管理后台日志记录。。。。。")
	public void test() {
		System.out.println("管理后台日志测试。。。。。。");
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		AdminLogTest adminLogTest = context.getBean(AdminLogTest.class);

		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.when(request.getHeader("X-Real-IP")).thenReturn("127.0.0.2");
		Mockito.when(request.getParameter("adminId")).thenReturn("test-admin-id");
		logger.info("ip为: " + request.getHeader("X-Real-IP"));

		adminLogTest.request = request;
		adminLogTest.test();
	}
}
