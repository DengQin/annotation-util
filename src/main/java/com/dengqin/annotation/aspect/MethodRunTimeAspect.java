package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.MethodRunTime;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/4/27.
 *
 * 接口耗时日志切面
 */
@Component
@Aspect
public class MethodRunTimeAspect {

	private static Logger logger = LoggerFactory.getLogger(MethodRunTimeAspect.class);

	@Around("@annotation(methodRunTime)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, MethodRunTime methodRunTime) throws Throwable {
		long start = System.currentTimeMillis();
		Object obj = null;
		try {
			obj = joinPoint.proceed();
		} finally {
			long usedTime = System.currentTimeMillis() - start;
			if (usedTime >= methodRunTime.usedTime()) {
				StringBuilder sb = new StringBuilder();
				sb.append("[").append(methodRunTime.value()).append("]");
				sb.append("[").append(joinPoint.getSignature()).append("]");
				sb.append("[").append(usedTime).append("ms]");
				logger.info(sb.toString());
			}
		}
		return obj;
	}
}
