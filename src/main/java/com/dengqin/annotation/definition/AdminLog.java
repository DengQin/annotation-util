package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * Created by dq on 2018/5/16.
 * 
 * 后台日志注解
 */
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AdminLog {

	String content();
}
