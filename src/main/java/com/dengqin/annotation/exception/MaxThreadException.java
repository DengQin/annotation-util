package com.dengqin.annotation.exception;

/**
 * Created by dq on 2018/5/23.
 * 
 * 最大线程数限制异常
 */
public class MaxThreadException extends RuntimeException {

	private static final long serialVersionUID = -4641431316629460743L;

	public MaxThreadException(String message) {
		super(message);
	}

}
