package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.IPDenied;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dq on 2018/5/10.
 * 
 * IP限制切面
 */
@Component
@Aspect
public class IPDeniedAspect {

	@Around("execution(* *.*(..,javax.servlet.http.HttpServletRequest)) && @annotation(ipDenied)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, IPDenied ipDenied) throws Throwable {
		checkIP(joinPoint.getArgs(), ipDenied);
		Object obj = joinPoint.proceed();
		return obj;
	}

	/**
	 * 验证IP
	 * 
	 * @param args
	 * @param ipDenied
	 */
	private void checkIP(Object[] args, IPDenied ipDenied) {
		if (args == null || args.length <= 0) {
			return;
		}
		HttpServletRequest request = null;
		for (Object arg : args) {
			if (arg instanceof HttpServletRequest) {
				request = (HttpServletRequest) arg;
				break;
			}
		}
		if (request == null) {
			return;
		}
		String currentIP = getIp(request);
		// IP校验
		for (String ip : ipDenied.ips().split(",")) {
			if (ip.equals(currentIP)) {
				return;
			}
		}
		throw new RuntimeException(ipDenied.message() + "，受限IP为： " + currentIP);
	}

	/**
	 * 获取真实IP,不使用X-Forwarded-For,用于权限判断
	 * 
	 * @param request
	 * @return
	 */
	private String getIp(HttpServletRequest request) {
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
