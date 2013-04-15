package com.chenjw.spider.dt.service;

import com.chenjw.spider.dt.model.SearchInfo;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.utils.Result;

public interface DeletedTweetReadService {

	public int countDeletedTweetsByUserId(SearchInfo searchInfo);

	public Result<TweetModel> findTopReposts(SearchInfo searchInfo);

	public PagedResult<TweetModel> findDeletedTweetsByUserId(
			SearchInfo searchInfo);

}
