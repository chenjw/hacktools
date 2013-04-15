package com.chenjw.spider.dt.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chenjw.spider.dt.dao.WatchedUserDAO;
import com.chenjw.spider.dt.dataobject.WatchedUserDO;

public class IbatisWatchedUserDAO extends SqlMapClientDaoSupport implements
		WatchedUserDAO {

	@Override
	public void addWatchedUser(WatchedUserDO watchedUser) {
		this.getSqlMapClientTemplate().insert("MS-INSERT-WATCHED-USER",
				watchedUser);
	}

	public void updateWatchedUser(WatchedUserDO watchedUser) {
		this.getSqlMapClientTemplate().update("MS-UPDATE-WATCHED-USER",
				watchedUser);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WatchedUserDO> getAllWatchedUsers() {
		return this.getSqlMapClientTemplate().queryForList(
				"MS-SELECT-ALL-WATCHED-USER");
	}

	@Override
	public WatchedUserDO findWatchedUser(String userId) {
		return (WatchedUserDO) this.getSqlMapClientTemplate().queryForObject(
				"MS-SELECT-WATCHED-USER", userId);
	}

	@Override
	public WatchedUserDO findWatchedUserByToken(String token) {
		return (WatchedUserDO) this.getSqlMapClientTemplate().queryForObject(
				"MS-SELECT-WATCHED-USER-BY-TOKEN", token);
	}

	@Override
	public WatchedUserDO findWatchedUserByScreenName(String screenName) {
		return (WatchedUserDO) this.getSqlMapClientTemplate().queryForObject(
				"MS-SELECT-WATCHED-USER-BY-SCREEN-NAME", screenName);
	}

}
