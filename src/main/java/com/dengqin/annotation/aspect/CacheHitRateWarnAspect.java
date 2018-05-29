package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.CacheHitRateWarn;
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
 * Created by dq on 2018/5/24.
 * 
 * 根据当前时间属于一天里的哪一分钟，统计该分钟内调用的次数。
 */
@Component
@Aspect
public class CacheHitRateWarnAspect {

	private static Logger logger = LoggerFactory.getLogger(CacheHitRateWarnAspect.class);

	// name -> 第几分钟:次数:已发报警
	private Map<String, String> threadMap = new HashMap<String, String>();

	@Around("@annotation(cacheHitRateWarn)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, CacheHitRateWarn cacheHitRateWarn) throws Throwable {
		this.statCall(cacheHitRateWarn);
		return joinPoint.proceed();
	}

	/**
	 * 统计缓存的调用情况
	 */
	public void statCall(CacheHitRateWarn cacheHitRateWarn) {
		// 当前时间在一天里的第几小时，比如当前时间为：2018-05-24 20:03:39，计算结果则为：20
		long curHour = DateUtils.getFragmentInHours(new Date(), Calendar.DATE);
		boolean needLog = false;
		int allTimes = 0;// 实际缓存总的命中次数，即调用次数
		int passTimes = 0;// 穿透缓存的次数
		synchronized (this) {
			String current = this.threadMap.get(cacheHitRateWarn.name());
			if (current == null || !current.startsWith("" + curHour + ':')) {
				if (current != null) {
					logger.info(current);
				}

				// 第x小时:总调用次数:穿透次数:已发报警
				current = "" + curHour + (cacheHitRateWarn.isPassCache() ? ":0:1:0" : ":1:0:0");
				this.threadMap.put(cacheHitRateWarn.name(), current);
				return;
			}

			String[] arr = current.split(":");
			allTimes = Integer.parseInt(arr[1]);
			passTimes = Integer.parseInt(arr[2]);
			if (cacheHitRateWarn.isPassCache()) {
				passTimes++;
			} else {
				allTimes++;
			}

			String warnFlag = arr[3];
			// 实际缓存总的调用次数大于每小时缓存最低调用次数&&（实际缓存总的调用次数*缓存命中率阀值）<实际的缓存穿透次数
			if (allTimes >= cacheHitRateWarn.minTimes() && (allTimes * cacheHitRateWarn.minRate() < passTimes)) {
				warnFlag = "1";
				if ("0".equals(arr[3])) {
					// 是否需要写日志
					needLog = true;
				}
			}
			current = "" + curHour + ':' + allTimes + ':' + passTimes + ':' + warnFlag;
			this.threadMap.put(cacheHitRateWarn.name(), current);
		}

		if (!needLog) {
			return;
		}
		String message = cacheHitRateWarn.name() + "缓存命中率低于预期:allTimes=" + allTimes + " passTimes=" + passTimes;
		if ("warn".equalsIgnoreCase(cacheHitRateWarn.logLevel())) {
			logger.warn(message);
		} else if ("error".equalsIgnoreCase(cacheHitRateWarn.logLevel())) {
			logger.error(message);
		} else {
			logger.info(message);
		}
	}
}
