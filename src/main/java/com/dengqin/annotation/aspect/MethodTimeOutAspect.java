package com.dengqin.annotation.aspect;

import com.dengqin.annotation.definition.MethodTimeOut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by dq on 2018/4/28.
 * 
 * 接口超时切面
 */
@Component
@Aspect
public class MethodTimeOutAspect {

	private static Logger logger = LoggerFactory.getLogger(MethodTimeOutAspect.class);

	@Around("@annotation(methodTimeOut)")
	public Object dealAspect(final ProceedingJoinPoint joinPoint, MethodTimeOut methodTimeOut) throws Throwable {
		ExecutorService executor = Executors.newCachedThreadPool();
		Future<Object> future = executor.submit(new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				try {
					return joinPoint.proceed();
				} catch (Throwable e) {
					throw new Exception(e.getMessage());
				}
			}
		});
		try {
			// InterruptedException, ExecutionException, TimeoutException
			return future.get(methodTimeOut.timeout(), TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder();
			sb.append("[").append(methodTimeOut.value()).append("]");
			sb.append("[").append(joinPoint.getSignature()).append("]");
			sb.append("[").append(methodTimeOut.timeout()).append("ms]");
			logger.error(sb.toString());
			throw e.getCause() != null ? e.getCause() : e;
		} finally {
			executor.shutdown();// shutdown() 方法在终止前允许执行以前提交的任务
			executor.shutdownNow();// shutdownNow() 方法阻止等待任务启动并试图停止当前正在执行的任务
		}
	}
}
