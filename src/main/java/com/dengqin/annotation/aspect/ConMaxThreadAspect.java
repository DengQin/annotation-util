package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.ConMaxThread;
import com.dengqin.annotation.exception.ConMaxThreadLimitException;
import org.apache.commons.lang.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dq on 2018/5/29.
 * 
 * 最大并发线程.约定第一个参数为监控点 维护每个监控点当前并发处理线程，该应用当前并发处理线程 。
 * 默认放行策略：设置警戒线程数（建议设为resin最大线程数*0.8） 未对具体活动设置最大服务线程的默认通过；
 * 针对具体活动设置了最大服务线程数actMax： 该活动服务线程 > actMax 且 当前总服务线程数 > 警戒线程数 ： 限制
 * 问题：总服务线程超过警戒，但该活动服务线程未过配置的阀值，仍会放行。这种情况应调整降低阀值，使之受限
 */
@Component
@Aspect
public class ConMaxThreadAspect {

	private static Logger logger = LoggerFactory.getLogger(ConMaxThreadAspect.class);

	// 当前服务线程数
	private static final AtomicInteger CURAPP_THREADCOUNT = new AtomicInteger(0);
	// 当前每监控点并发线程
	private static final Map<String, AtomicInteger> MONITOR_POINT_THREAD_MAP = new HashMap<String, AtomicInteger>();
	// 每分钟统计丢弃的次数
	private static final Map<Integer, Map<String, AtomicInteger>> DISCARD_STAT = new HashMap<Integer, Map<String, AtomicInteger>>(
			1440);
	// 对监控点设置的线程限制
	private static Map<String, Integer> SETTING_MAP = new HashMap<String, Integer>();

	@Around("@annotation(conMaxThread)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, ConMaxThread conMaxThread) throws Throwable {
		Object[] args = joinPoint.getArgs();

		if (args == null || args.length < 1) {
			return joinPoint.proceed();
		}
		String entryKey = getEntryKey(args, conMaxThread.entry());
		boolean enterSuccess = false;
		try {
			enterSuccess = addReqTimes(entryKey, conMaxThread.warnThreadNum());
			if (!enterSuccess) {
				throw new ConMaxThreadLimitException("并发线程受限:" + entryKey);
			}
			return joinPoint.proceed();
		} finally {
			if (enterSuccess) {
				subReqTimes(entryKey);
			}
		}
	}

	/**
	 * 取监控点
	 */
	private String getEntryKey(Object[] args, String entry) {
		String replaceValue = String.valueOf(args[0]);
		return StringUtils.replace(entry, "${0}", replaceValue).toLowerCase();
	}

	/**
	 * 增加请求次数
	 *
	 * @param entryKey
	 * @return
	 */
	private boolean addReqTimes(String entryKey, int warnThreadNum) {
		// 当前计数
		AtomicInteger curCounter = MONITOR_POINT_THREAD_MAP.get(entryKey);
		if (curCounter == null) {
			curCounter = new AtomicInteger(0);
			MONITOR_POINT_THREAD_MAP.put(entryKey, curCounter);
		}
		// 针对具体活动设置了最大服务线程数actMax： 该活动服务线程 > actMax 且 当前总服务线程数 > 警戒线程数 ： 限制
		boolean isLimitReq = false;
		if (SETTING_MAP.containsKey(entryKey) && SETTING_MAP.get(entryKey) <= curCounter.get()) {
			if (CURAPP_THREADCOUNT.get() >= warnThreadNum) {
				isLimitReq = true;
			} else {
				// logger.debug("线程" + entryKey + "并发超过配置值" +
				// SETTING_MAP.get(entryKey) + "但应用线程未超警戒,当前:" +
				// CURAPP_THREADCOUNT.get());
			}
		}

		if (!isLimitReq) {
			// 当前没有受限
			// logger.debug("increment:" + entryKey);
			CURAPP_THREADCOUNT.incrementAndGet();
			curCounter.incrementAndGet();
			return true;
		} else {
			// 统计丢弃请求
			// 当前时间在一天中的分钟数
			addDiscard(entryKey);
			return false;
		}
	}

	/**
	 * 增加丢弃统计
	 *
	 * @param entryKey
	 */
	private void addDiscard(String entryKey) {
		int minute = (int) DateUtils.getFragmentInMinutes(new Date(), Calendar.DATE);
		Map<String, AtomicInteger> statMap = DISCARD_STAT.get(minute);
		if (statMap == null) {
			statMap = new HashMap<String, AtomicInteger>();
			DISCARD_STAT.put(minute, statMap);
		}
		AtomicInteger curCounter = statMap.get(entryKey);
		if (curCounter == null) {
			curCounter = new AtomicInteger(0);
			statMap.put(entryKey, curCounter);

			// 0次，移除前100分钟的10条记录
			int fromMinute = minute >= 110 ? minute - 110 : (1440 + minute - 110);
			for (int i = 0; i < 10; i++) {
				DISCARD_STAT.remove(fromMinute + i);
			}
		} else {
			curCounter.incrementAndGet();
		}
	}

	/**
	 * 减少请求次数
	 *
	 * @param entryKey
	 * @return
	 */
	private void subReqTimes(String entryKey) {
		// logger.debug("decrement:" + entryKey);
		CURAPP_THREADCOUNT.decrementAndGet();
		// 当前计数
		AtomicInteger curCounter = MONITOR_POINT_THREAD_MAP.get(entryKey);
		if (curCounter != null) {
			curCounter.decrementAndGet();
		} else {
			logger.warn("线程计数怎么为空？" + entryKey);
		}
	}

	/**
	 * 实始化配置
	 *
	 * @param settingList
	 */
	static public void initSetting(List<MonitorPoint> settingList) {
		Map<String, Integer> tempMap = new HashMap<String, Integer>();
		for (MonitorPoint monitorPoint : settingList) {
			tempMap.put(monitorPoint.getPoint().toLowerCase().trim(), monitorPoint.getLimitCount());
		}

		logger.info("重设并发线程限制:" + tempMap);
		SETTING_MAP = tempMap;
	}

	static private Map<String, Integer> _convert(Map<String, AtomicInteger> map) {
		Map<String, Integer> retMap = new HashMap<String, Integer>();
		for (Map.Entry<String, AtomicInteger> entry : map.entrySet()) {
			retMap.put(entry.getKey(), entry.getValue().get());
		}
		return retMap;
	}

	/**
	 * 取监控结果
	 *
	 * @return
	 */
	static public Map<String, Integer> getMonitorStat() {
		Map<String, AtomicInteger> tempMap = new HashMap<String, AtomicInteger>();
		tempMap.putAll(MONITOR_POINT_THREAD_MAP);
		tempMap.put("CURAPP_THREADCOUNT", CURAPP_THREADCOUNT);
		return _convert(tempMap);
	}

	/**
	 * 取丢弃请求统计结果 取多少分钟以来的
	 * 
	 * @return
	 */
	static public Map<String, Integer> getDiscardStat(int minuteCount) {
		int minute = (int) DateUtils.getFragmentInMinutes(new Date(), Calendar.DATE);
		Map<String, AtomicInteger> tempMap = new HashMap<String, AtomicInteger>();
		for (int i = 0; i < minuteCount; i++) {
			// 0次，移除前100分钟的10条记录
			int getMinute = minute - i;
			if (getMinute < 0) {
				getMinute = getMinute + 1440;
			}
			Map<String, AtomicInteger> tempMap2 = DISCARD_STAT.get(getMinute);
			if (tempMap2 != null) {
				tempMap.putAll(tempMap2);
			}
		}

		return _convert(tempMap);
	}

	public static class MonitorPoint {

		private String point;
		private int limitCount;

		/**
		 * @return the point
		 */
		public String getPoint() {
			return point;
		}

		/**
		 * @param point
		 *            the point to set
		 */
		public void setPoint(String point) {
			this.point = point;
		}

		/**
		 * @return the limitCount
		 */
		public int getLimitCount() {
			return limitCount;
		}

		/**
		 * @param limitCount
		 *            the limitCount to set
		 */
		public void setLimitCount(int limitCount) {
			this.limitCount = limitCount;
		}
	}
}
