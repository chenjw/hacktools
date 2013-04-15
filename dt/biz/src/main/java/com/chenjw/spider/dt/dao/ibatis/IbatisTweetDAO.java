package com.chenjw.spider.dt.dao.ibatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.chenjw.spider.dt.dao.TweetDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.model.TweetModel;

public class IbatisTweetDAO extends SqlMapClientDaoSupport implements TweetDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<TweetDO> findByMemberUserId(String memberUserId) {
		return this.getSqlMapClientTemplate().queryForList(
				"MS-SELECT-TWEET-IDS-BY-MEMBER-USER-ID", memberUserId);
	}

	@Override
	public TweetDO findByTidAndMemberUserId(String tid, String memberUserId) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("tid", tid);
		p.put("memberUserId", memberUserId);
		return (TweetDO) this.getSqlMapClientTemplate().queryForObject(
				"MS-SELECT-TWEET-BY-TID-AND-MEMBER-USER-ID", p);
	}

	@Override
	public void addTweet(TweetDO tweet) {
		TweetModel model = new TweetModel();
		TweetMapper.do2Model(tweet, model);
		// String str = "[add-tweet] "
		// + model.getId()
		// + " "
		// + DateUtils.toLocaleString(model.getCreatedAt(),
		// "yyyy-MM-dd HH:mm:ss") + " ["
		// + model.getUser().getScreenName() + "] " + model.getText();
		// System.out.println(str);
		this.getSqlMapClientTemplate().insert("MS-INSERT-TWEET", tweet);
	}

	@Override
	public void deleteByTidAndMemberUserId(String tid, String memberUserId) {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("tid", tid);
		p.put("memberUserId", memberUserId);
		this.getSqlMapClientTemplate().delete(
				"MS-DELETE-TWEET-BY-TID-AND-MEMBER-USER-ID", p);
	}

}
