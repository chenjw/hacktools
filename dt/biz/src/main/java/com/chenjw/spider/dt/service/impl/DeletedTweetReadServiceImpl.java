package com.chenjw.spider.dt.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.model.SearchInfo;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.utils.Result;
import com.chenjw.tools.BeanCopyUtils;

public class DeletedTweetReadServiceImpl implements DeletedTweetReadService {

	private DeletedTweetDAO deletedTweetDAO;

	@Override
	public Result<TweetModel> findTopReposts(SearchInfo searchInfo) {
		Result<TweetModel> r = new Result<TweetModel>();
		List<TweetModel> l = new ArrayList<TweetModel>();
		List<TweetDO> dos = deletedTweetDAO.findTopReposts(searchInfo);
		for (TweetDO d : dos) {
			TweetModel model = new TweetModel();
			TweetMapper.do2Model(d, model);
			l.add(model);
		}
		r.setList(l);

		return r;
	}

	@Override
	public PagedResult<TweetModel> findDeletedTweetsByUserId(
			SearchInfo searchInfo) {
		PagedResult<TweetModel> r = new PagedResult<TweetModel>();
		List<TweetModel> l = new ArrayList<TweetModel>();
		List<TweetDO> list = deletedTweetDAO.findByMemberUserId(searchInfo);
		for (TweetDO d : list) {
			TweetModel model = new TweetModel();
			TweetMapper.do2Model(d, model);
			l.add(model);
		}
		r.setList(l);
		if (l.size() < searchInfo.getPage().getPageSize()) {
			r.setMoreNum(0);
		} else {
			SearchInfo countInfo = new SearchInfo();
			BeanCopyUtils.copyProperties(countInfo, searchInfo);
			Page page = new Page();
			page.setMaxSort(l.get(l.size() - 1).getDeleteSort());
			countInfo.setPage(page);
			int moreNum = deletedTweetDAO.countByMemberUserId(countInfo);
			r.setMoreNum(moreNum);
		}

		return r;
	}

	@Override
	public int countDeletedTweetsByUserId(SearchInfo searchInfo) {
		int num = deletedTweetDAO.countByMemberUserId(searchInfo);
		return num;
	}

	public void setDeletedTweetDAO(DeletedTweetDAO deletedTweetDAO) {
		this.deletedTweetDAO = deletedTweetDAO;
	}

}
