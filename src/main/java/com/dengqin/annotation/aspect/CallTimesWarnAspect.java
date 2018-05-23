package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.CallTimesWarn;
import org.apache.commons.lang.time.DateUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dq on 2018/5/23.
 * 
 * 根据当前时间属于一天里的哪一分钟，统计该分钟内调用的次数的切面。
 */
@Component
@Aspect
public class CallTimesWarnAspect {

	private static Logger logger = LoggerFactory.getLogger(CallTimesWarnAspect.class);

	// name -> 第几分钟:次数:已发报警
	private Map<String, String> threadMap = new HashMap<String, String>();

	@Around("@annotation(callTimesWarn)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, CallTimesWarn callTimesWarn) throws Throwable {
		this.statCall(callTimesWarn.name(), callTimesWarn.warnTimes(), callTimesWarn.logLevel());
		return joinPoint.proceed();
	}

	/**
	 * 统计调用的次数
	 */
	public void statCall(String name, int warnTimes, String logLevel) {
		// 当前时间在一天里的第几分钟
		// 比如：当前时间为：2018-05-23 20:26:13，那么计算结果为：1226=（20h*60）+26min
		long curMinute = DateUtils.getFragmentInMinutes(new Date(), Calendar.DAY_OF_YEAR);
		boolean needLog = false; // 是否需要打印日志
		synchronized (this) {
			String current = this.threadMap.get(name);
			if (current == null || !current.startsWith("" + curMinute + ':')) {
				// 第几分钟:次数:已发报警
				current = "" + curMinute + ":1:0";
				this.threadMap.put(name, current);
				return;
			}

			String[] arr = current.split(":");
			int times = Integer.parseInt(arr[1]);
			if (times >= warnTimes && "0".equals(arr[2])) {
				// 未写日志
				needLog = true;
			}
			current = "" + curMinute + ':' + (times + 1) + ":" + (times >= warnTimes ? "1" : "0");
			this.threadMap.put(name, current);
		}

		if (!needLog) {
			return;
		}

		String message = name + "调用次数超过" + warnTimes + "次";
		if ("warn".equalsIgnoreCase(logLevel)) {
			logger.warn(message);
		} else if ("error".equalsIgnoreCase(logLevel)) {
			logger.error(message);
		} else {
			logger.info(message);
		}
	}
}
