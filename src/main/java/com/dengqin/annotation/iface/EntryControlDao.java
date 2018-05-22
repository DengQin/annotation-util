package com.dengqin.annotation.iface;

/**
 * Created by dq on 2018/5/21.
 * 
 * 入口控制服务接口（比如可以采用Redis来实现这个接口进行控制）
 */
public interface EntryControlDao {

	/**
	 * 尝试进入某入口，并最多锁定maxLockTime秒
	 * 
	 * @param entryKey
	 * @param maxLockTime
	 * @return
	 */
	boolean canEnter(String entryKey, int maxLockTime);

	/**
	 * 清除某入口进入痕迹
	 * 
	 * @param entryKey
	 */
	boolean cleanEnter(String entryKey);
}
