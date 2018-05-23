package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.MaxThread;
import com.dengqin.annotation.exception.MaxThreadException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dq on 2018/5/23.
 * 
 * 限制最大线程数切面
 */
@Component
@Aspect
public class MaxThreadAspect {

	private Map<String, Integer> threadMap = new HashMap<String, Integer>();

	@Around("@annotation(maxThread)")
	public Object dealAspect(ProceedingJoinPoint joinPoint, MaxThread maxThread) throws Throwable {
		String name = maxThread.name();
		int max = Integer.valueOf(maxThread.max());
		this.addThreads(name, max);
		Object obj = null;
		try {
			obj = joinPoint.proceed();
		} finally {
			this.removeThreads(name);
		}
		return obj;
	}

	/**
	 * 添加线程数量
	 */
	public void addThreads(String name, int max) {
		synchronized (this) {
			Integer current = this.threadMap.get(name);
			if (current == null) {
				current = 0;
			}
			if (current >= max) {
				throw new MaxThreadException(
						"超出限制连接数量，interface[" + name + "],current[" + current + "],max[" + max + "]");
			}
			current += 1;
			this.threadMap.put(name, current);
			// System.out.println("正常添加，interface[" + name + "],current[" +
			// current + "],max[" + max + "]");
		}
	}

	/**
	 * 减少线程的数量
	 */
	public void removeThreads(String name) {
		synchronized (this) {
			Integer current = this.threadMap.get(name);
			if (current == null || current == 0) {
				throw new MaxThreadException("非法连接数量，interface[" + name + "],current[" + current + "]");
			}
			current -= 1;
			this.threadMap.put(name, current);
			// System.out.println("正常减少，interface[" + name + "],current[" +
			// current + "]");
		}
	}
}
