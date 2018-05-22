package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by dq on 2018/4/28.
 * 
 * 接口的超时注解
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodTimeOut {

	String value() default "接口运行超时";

	/** 默认3秒超时,单位ms */
	long timeout() default 3000;
}
