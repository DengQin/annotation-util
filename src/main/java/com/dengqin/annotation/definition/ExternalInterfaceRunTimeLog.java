package com.dengqin.annotation.definition;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * 外部接口运行耗时日志注解
 * 
 * Created by dq on 2018/5/21.
 */
@Target({ METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ExternalInterfaceRunTimeLog {

	/** UDB登录验证 */
	String UDB_LOGIN_CHECK = "UDB_LOGIN_CHECK";

	String interfaceName();

	String defUri() default "unkownUri";

}
