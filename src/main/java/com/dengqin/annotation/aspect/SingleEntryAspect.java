package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.SingleEntry;
import com.dengqin.annotation.exception.ConcurrentLimitException;
import com.dengqin.annotation.iface.EntryControlDao;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/5/22.
 * 
 * 方法单入切面
 */
@Component
@Aspect
public class SingleEntryAspect {

	private static Logger logger = LoggerFactory.getLogger(MethodTimeOutAspect.class);

	@Autowired(required = false)
	private EntryControlDao entryControlDao;

	@Around("@annotation(singleEntry)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, SingleEntry singleEntry) throws Throwable {
		Object[] args = joinPoint.getArgs();
		if (args == null || args.length < 1) {
			return joinPoint.proceed();
		}
		String entryKey = getEntryKey(args, singleEntry.entry());
		boolean enterSuccess = false;
		try {
			enterSuccess = entryControlDao.canEnter(entryKey, singleEntry.maxLockSecond());
			if (!enterSuccess) {
				logger.info("尝试获取单入锁失败:" + entryKey);
				throw new ConcurrentLimitException("尝试获取单入锁失败:" + entryKey);
			}
			return joinPoint.proceed();
		} finally {
			if (enterSuccess) {
				// 执行完毕之后要释放锁资源
				entryControlDao.cleanEnter(entryKey);
			}
		}
	}

	/**
	 * 拼接组合成锁的key值
	 * 
	 * @param args
	 *            方法入参
	 * @param entry
	 *            占位符指定的参数实体
	 * @return
	 */
	private String getEntryKey(Object[] args, String entry) {
		int argLen = args.length;
		for (int i = 0; i <= 9 && i <= argLen; i++) {
			if (entry.contains("${" + i + "}")) {
				String replaceValue = String.valueOf(args[i]);
				entry = StringUtils.replace(entry, "${" + i + "}", replaceValue);
			}
		}
		return entry;
	}

}
