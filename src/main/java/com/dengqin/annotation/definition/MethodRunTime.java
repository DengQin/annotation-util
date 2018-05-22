package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by dq on 2018/4/27.
 * 
 * 运行耗时注解
 */
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MethodRunTime {

	/** 运行名称 */
	String value() default "接口运行耗时";

	/** 运行日志阀值，大于这个值就会输出耗时日志，小于就不会输出，单位:ms */
	long usedTime() default 10;
}
