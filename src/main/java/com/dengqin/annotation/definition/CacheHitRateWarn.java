package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by dq on 2018/5/24.
 * 
 * 缓存命中率报警器注解，每小时统计缓存命中率，低于预期发警报
 */
@Target({ METHOD, TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheHitRateWarn {

	String name();

	/** 每小时最低调用次数 */
	int minTimes() default 10000;

	/** 预期警报命中率 */
	double minRate() default 0.9;

	/** 当前调用是否被穿透 */
	boolean isPassCache();

	/** 警报级别:info,warn,error */
	String logLevel();
}
