package com.chenjw.spider.aliuser.dao;

import com.chenjw.spider.aliuser.model.AliUserInfo;


public interface AliUserDAO {
	/**
	 * 获得保存的最后一个用户信息
	 * 
	 * @return
	 */
	public AliUserInfo getLastUserInfo();

	/**
	 * 保存用户信息
	 * 
	 * @param userInfo
	 */
	public void save(AliUserInfo userInfo);
}
