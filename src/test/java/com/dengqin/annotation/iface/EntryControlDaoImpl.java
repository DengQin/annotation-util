package com.dengqin.annotation.iface;

import org.springframework.stereotype.Component;

/**
 * Created by dq on 2018/5/22.
 */
@Component
public class EntryControlDaoImpl implements EntryControlDao {

	@Override
	public boolean canEnter(String entryKey, int maxLockTime) {
		System.out.println("ddddd");
		return false;
	}

	@Override
	public boolean cleanEnter(String entryKey) {
		System.out.println("aaaaaaaaaaaaa");
		return false;
	}
}
