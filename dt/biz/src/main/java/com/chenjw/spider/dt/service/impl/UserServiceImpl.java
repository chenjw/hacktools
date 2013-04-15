package com.chenjw.spider.dt.service.impl;

import com.chenjw.spider.dt.constants.UserStatusEnum;
import com.chenjw.spider.dt.dao.WatchedUserDAO;
import com.chenjw.spider.dt.dataobject.WatchedUserDO;
import com.chenjw.spider.dt.mapper.TokenMapper;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;

public class UserServiceImpl implements UserService {
	private WatchedUserDAO watchedUserDAO;
	private WeiboService weiboService;

	public void updateUser(TokenModel model) {
		WatchedUserDO newUser = new WatchedUserDO();
		TokenMapper.model2Do(model, newUser);
		watchedUserDAO.updateWatchedUser(newUser);
	}

	@Override
	public TokenModel addUser(String token,String clientId,String clientSecret) {
		String userId = weiboService.findUserIdByToken(token);
		UserModel user = weiboService.findUserByUserId(userId, token);
		WatchedUserDO oldUser = watchedUserDAO.findWatchedUser(userId);
		TokenModel tokenModel = new TokenModel();
		if (oldUser == null) {
			WatchedUserDO newUser = new WatchedUserDO();
			newUser.setUserId(userId);
			newUser.setToken(token);
			newUser.setStatus(UserStatusEnum.FOREVER_VALID.name());
			newUser.setScreenName(user.getScreenName());
			newUser.setClientId(clientId);
			newUser.setClientSecret(clientSecret);
			watchedUserDAO.addWatchedUser(newUser);
		} else {
			// 如果token已更新，更新db中的token
			if (!token.equals(oldUser.getToken())) {
				oldUser.setToken(token);
				oldUser.setClientId(clientId);
				oldUser.setClientSecret(clientSecret);
				watchedUserDAO.updateWatchedUser(oldUser);
			}
		}
		TokenMapper.do2Model(oldUser, tokenModel);
		return tokenModel;

	}

	@Override
	public TokenModel findWatchedUserById(String userId) {
		WatchedUserDO d = watchedUserDAO.findWatchedUser(userId);
		if (d == null) {
			return null;
		} else {
			TokenModel m = new TokenModel();
			TokenMapper.do2Model(d, m);
			return m;
		}
	}
	
	@Override
	public TokenModel findWatchedUserByScreenName(String screenName) {
		WatchedUserDO d = watchedUserDAO.findWatchedUserByScreenName(screenName);
		if (d == null) {
			return null;
		} else {
			TokenModel m = new TokenModel();
			TokenMapper.do2Model(d, m);
			return m;
		}
	}


	@Override
	public TokenModel findWatchedUserByToken(String token) {
		String userId = weiboService.findUserIdByToken(token);
		WatchedUserDO d = watchedUserDAO.findWatchedUser(userId);
		if (d == null) {
			return null;
		} else {
			TokenModel m = new TokenModel();
			TokenMapper.do2Model(d, m);
			return m;
		}
	}

	@Override
	public TokenModel updatePeriodValid(String token, int days) {
		throw new java.lang.UnsupportedOperationException();
	}

	public void setWatchedUserDAO(WatchedUserDAO watchedUserDAO) {
		this.watchedUserDAO = watchedUserDAO;
	}

	public void setWeiboService(WeiboService weiboService) {
		this.weiboService = weiboService;
	}

}
