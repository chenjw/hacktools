package com.chenjw.spider.dt.service;

import com.chenjw.spider.dt.model.TokenModel;

public interface UserService {

	public void updateUser(TokenModel model);

	public TokenModel addUser(String token,String clientId,String clientSecret);

	public TokenModel updatePeriodValid(String token, int days);

	public TokenModel findWatchedUserByToken(String token);

	public TokenModel findWatchedUserById(String userId);
	
	public TokenModel findWatchedUserByScreenName(String screenName);

}
