package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.ExternalInterfaceRunTimeLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dq on 2018/5/21.
 * 
 * 外部接口运行耗时日志切面
 */
@Component
@Aspect
public class ExternalInterfaceRunTimeLogAspect {

	private static Logger logger = LoggerFactory.getLogger(ExternalInterfaceRunTimeLogAspect.class);

	@Autowired(required = false)
	private HttpServletRequest request;

	@Around("@annotation(externalInterfaceRunTimeLog)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, ExternalInterfaceRunTimeLog externalInterfaceRunTimeLog)
			throws Throwable {
		long start = System.currentTimeMillis();
		Object obj;
		try {
			obj = joinPoint.proceed();
		} finally {
			long usedTime = System.currentTimeMillis() - start;

			// if (RequestContextHolder.getRequestAttributes() != null ) {
			// //默认情况下spring会注入request代理类，通过线程上下文中获取attributes表的内容判断是否是通过RequestContextFilter注入request对象；
			dealLog(externalInterfaceRunTimeLog.interfaceName(), externalInterfaceRunTimeLog.defUri(), usedTime);
			// long totalTime =
			// NumberUtils.toLong(request.getAttribute("_runtime") + "") +
			// usedTime;
			// request.setAttribute("_runtime", totalTime);
			// }
			// logger.info(new
			// StringBuilder().append("[").append(joinPoint.getSignature()).append("][").append(usedTime).append("ms]").toString());
		}
		return obj;
	}

	/**
	 * 处理日志： 由于定时间调用无外部IP和地直源用默认地址；
	 *
	 * @param interfaceName
	 * @param usedTime
	 */
	private void dealLog(String interfaceName, String defUri, long usedTime) {
		StringBuilder sb = new StringBuilder();
		String realIp = "localhost", url = "localhost";
		try {
			realIp = getRealIp(request, "127.0.0.1");
			url = getURI(request, defUri);
		} catch (Exception e) {
		}

		sb.append(realIp).append(" ").append(usedTime).append(" ").append(interfaceName).append(" ").append(url)
				.append(" ");
		logger.info(sb.toString());
		// System.out.println("sb:" + sb + ",defUri:" + defUri + ",request:" +
		// request);
	}

	/**
	 * 获取真实IP,不使用X-Forwarded-For[用于权限判断]
	 *
	 * @param request
	 * @return
	 */
	private String getRealIp(HttpServletRequest request, String defIp) {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return defIp;
		}
		String ip = "";
		ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("RealIP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取请求地址
	 *
	 * @param request
	 * @return
	 */
	private String getURI(HttpServletRequest request, String defUri) {
		if (RequestContextHolder.getRequestAttributes() == null) {
			return defUri;
		}
		return request.getRequestURI();
	}

}
