package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by dq on 2018/5/29.
 * 
 * 最大并发线程注解
 */
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ConMaxThread {

	/**
	 * 入口配置，支持${1}占位符替换第1个方法参数
	 * 
	 * @return
	 */
	String entry();

	/**
	 * 预警线程数，建议0.8*最大线程数
	 * 
	 * @return
	 */
	int warnThreadNum() default 1600;
}
