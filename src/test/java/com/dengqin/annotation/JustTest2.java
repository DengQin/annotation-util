package com.dengqin.annotation;

import org.apache.commons.lang.StringUtils;

/**
 * Created by dq on 2018/5/22.
 */
public class JustTest2 {
	public static void main(String[] args) {
		String entry = "hehe";
		String replaceValue = "worlf";
		entry = StringUtils.replace(entry, "eh", replaceValue);
		System.out.println(entry);
	}
}
