package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by dq on 2018/5/21.
 *
 * 单入注解
 */

@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SingleEntry {

	/**
	 * 入口配置，支持${1}..${9}占位符替换方法参数，${0}代表方法第一个参数，${1}代表方法的第二个参数
	 * 
	 * @return
	 */
	String entry();

	/**
	 * 最大锁定时间单位：秒，默认10分钟
	 * 
	 * @return
	 */
	int maxLockSecond() default 600;

}
