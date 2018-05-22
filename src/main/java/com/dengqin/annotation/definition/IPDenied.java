package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by dq on 2018/4/28.
 * 
 * IP限制注解
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IPDenied {

	/** 允许开放的IP，逗号分割；默认开放本机 */
	String ips() default "127.0.0.1";

	/** 限制信息 */
	String message() default "IP denied";
}
