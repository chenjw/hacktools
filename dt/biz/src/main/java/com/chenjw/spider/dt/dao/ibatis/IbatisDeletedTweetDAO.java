package com.chenjw.spider.dt.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.model.SearchInfo;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.tools.beancopy.util.DateUtils;

public class IbatisDeletedTweetDAO extends SqlMapClientDaoSupport implements
		DeletedTweetDAO {

	@Override
	public TweetDO findByTidAndMemberUserId(String tid, String memberUserId) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("tid", tid);
		p.put("memberUserId", memberUserId);
		return (TweetDO) this.getSqlMapClientTemplate().queryForObject(
				"MS-SELECT-DELETED-TWEET-BY-TID-AND-MEMBER-USER-ID", p);
	}

	@SuppressWarnings("unchecked")
	public List<TweetDO> findByMemberUserId(SearchInfo searchInfo) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("followerUserId", searchInfo.getFollowerUserId());
		p.put("senderUserId", searchInfo.getSenderUserId());
		p.put("page", searchInfo.getPage());
		return this.getSqlMapClientTemplate().queryForList(
				"MS-SELECT-DELETED-TWEET-BY-MEMBER-USER-ID", p);

	}

	@SuppressWarnings("unchecked")
	public List<TweetDO> findTopReposts(SearchInfo searchInfo) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("followerUserId", searchInfo.getFollowerUserId());
		p.put("senderUserId", searchInfo.getSenderUserId());
		return this.getSqlMapClientTemplate().queryForList(
				"MS-SELECT-DELETED-TWEET-TOP-REPOSTS", p);

	}

	public int countByMemberUserId(SearchInfo searchInfo) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("followerUserId", searchInfo.getFollowerUserId());
		p.put("senderUserId", searchInfo.getSenderUserId());
		p.put("minSort", searchInfo.getPage().getMinSort());
		p.put("maxSort", searchInfo.getPage().getMaxSort());
		return (Integer) this.getSqlMapClientTemplate().queryForObject(
				"MS-COUNT-DELETED-TWEET-BY-MEMBER-USER-ID", p);
	}

	@Override
	public void addTweet(TweetDO tweet) {
		TweetModel model = new TweetModel();
		TweetMapper.do2Model(tweet, model);
		String str = "[add-deleted-tweet] "
				+ model.getId()
				+ " "
				+ DateUtils.toLocaleString(model.getCreatedAt(),
						"yyyy-MM-dd HH:mm:ss") + " ["
				+ model.getUser().getScreenName() + "] " + model.getText();
		System.out.println(str);
		this.getSqlMapClientTemplate().insert("MS-INSERT-DELETED-TWEET", tweet);
	}

	@Override
	public void deleteByTidAndMemberUserId(String tid, String memberUserId) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("tid", tid);
		p.put("memberUserId", memberUserId);
		this.getSqlMapClientTemplate().delete(
				"MS-DELETE-DELETED-TWEET-BY-TID-AND-MEMBER-USER-ID", p);
	}

}
