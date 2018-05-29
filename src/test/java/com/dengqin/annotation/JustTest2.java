package com.dengqin.annotation;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by dq on 2018/5/22.
 */
public class JustTest2 {
	public static void main(String[] args) {
		String entry = "hehe";
		String replaceValue = "worlf";
		entry = StringUtils.replace(entry, "eh", replaceValue);
		System.out.println(entry);

		System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		System.out.println("时间：" + DateUtils.getFragmentInMinutes(new Date(), Calendar.DAY_OF_YEAR));
		System.out.println("小时：" + DateUtils.getFragmentInHours(new Date(), Calendar.DATE));
	}
}
