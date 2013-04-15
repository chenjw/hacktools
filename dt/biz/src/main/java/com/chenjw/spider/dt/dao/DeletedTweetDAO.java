package com.chenjw.spider.dt.dao;

import java.util.List;

import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.model.SearchInfo;

public interface DeletedTweetDAO {
	public void addTweet(TweetDO tweet);

	public void deleteByTidAndMemberUserId(String tid, String memberUserId);

	public TweetDO findByTidAndMemberUserId(String tid, String memberUserId);

	public List<TweetDO> findByMemberUserId(SearchInfo searchInfo);

	public int countByMemberUserId(SearchInfo searchInfo);

	public List<TweetDO> findTopReposts(SearchInfo searchInfo);
}
