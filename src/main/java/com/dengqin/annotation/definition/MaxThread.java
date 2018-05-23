package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by dq on 2018/5/22.
 * 
 * 最大线程数量限制注解
 */
@Target({ METHOD, TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MaxThread {

	/** 名称 */
	String name();

	/** 最大数量 */
	String max();
}
