package com.dengqin.annotation.exception;

/**
 * Created by dq on 2018/5/22.
 * 
 * 并发受限制异常
 */
public class ConcurrentLimitException extends RuntimeException {

	public ConcurrentLimitException(String message) {
		super(message);
	}
}
