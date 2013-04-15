package com.chenjw.spider.dt.service;

import java.util.List;

import weibo4j.model.WeiboException;

import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;

public interface WeiboService {
	public String findUserIdByToken(String token);

	public String parseSignedRequest(String signedRequest,String clientId,String clientSecret);

	public String findAuthorizeUrl();
	
	public String findAuthorizeUrl(String clientId);

	public String findAccessTokenByCode(String code,String clientId,String clientSecret);

	public List<TweetModel> findUserTimelineByUserId(String userId,
			String token, long sinceId);

	public List<TweetModel> findFriendsTimeline(TokenModel user, long sinceId);

	public UserModel findUserByName(String name, String token);

	public UserModel findUserByUserId(String userId, String token);

	public String[] findFriendIdsByUserId(String userId, String token);

	public String findOriginStatusUrl(TokenModel user, String tid);

	public TweetModel upload(TokenModel user, String text, byte[] img)
			throws WeiboException;
	
	
	/**
	 * 最热门的微薄
	 * 
	 * @return
	 */
	public List<TweetModel> findTopTimeline();
}
