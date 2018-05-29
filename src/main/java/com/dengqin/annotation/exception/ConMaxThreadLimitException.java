package com.dengqin.annotation.exception;

/**
 * Created by dq on 2018/5/29.
 * 
 * 并发服务线程受限
 */
public class ConMaxThreadLimitException extends RuntimeException {

	public ConMaxThreadLimitException(String message) {
		super(message);
	}
}
