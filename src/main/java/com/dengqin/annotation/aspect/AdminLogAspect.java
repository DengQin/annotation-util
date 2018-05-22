package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.AdminLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dq on 2018/5/16.
 * 
 * 后台日志切面
 */
@Component
@Aspect
public class AdminLogAspect {

	private static Logger logger = LoggerFactory.getLogger(AdminLogAspect.class);

	@Autowired(required = false)
	private HttpServletRequest request;

	@Around("@annotation(adminLog)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, AdminLog adminLog) throws Throwable {
		Object obj = null;
		try {
			obj = joinPoint.proceed();
		} finally {
			dealLog(adminLog.content());
		}
		return obj;
	}

	private void dealLog(String content) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("[").append(getRealIp(request)).append("][").append(content).append("][")
					.append(request.getParameter("adminId")).append("]");
			logger.info(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取真实IP,不使用X-Forwarded-For[用于权限判断]
	 *
	 * @param request
	 */
	private String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
