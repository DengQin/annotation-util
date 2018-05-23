package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by dq on 2018/5/23.
 * 
 * 统计每分钟达到多少就报警注解
 */
@Target({ METHOD, TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CallTimesWarn {

	String name();

	/** 达到报警的阀值次数，默认达到60次报警 */
	int warnTimes() default 60;

	/** 警报级别:info,warn,error */
	String logLevel();
}
