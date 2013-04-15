package com.chenjw.spider.dt.mapper;

import java.io.UnsupportedEncodingException;

import weibo4j.model.Status;

import com.alibaba.fastjson.JSON;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.model.ReasonModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.tools.BeanCopyUtils;

public class TweetMapper {
	public static void model2Do(TweetModel model, TweetDO dataobject) {
		String content = JSON.toJSONString(model);
		if (content != null) {
			try {
				dataobject.setContent(content.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		dataobject.setTid(model.getId());
		dataobject.setUserId(model.getUser().getId());
		dataobject.setPostDate(model.getCreatedAt());
		dataobject.setDeleteDate(model.getDeleteDate());
		dataobject.setDeleteSort(model.getDeleteSort());
		dataobject.setRepostsCount(model.getRepostsCount());
		dataobject.setCommentsCount(model.getCommentsCount());
	}

	public static void do2Model(TweetDO dataobject, TweetModel model) {
		TweetModel dbModel = null;
		try {
			dbModel = JSON.parseObject(new String(dataobject.getContent(),
					"UTF-8"), TweetModel.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		BeanCopyUtils.copyProperties(model, dbModel);
		model.setDeleteDate(dataobject.getDeleteDate());
		model.setDeleteSort(dataobject.getDeleteSort());
	}

	public static void wbStatus2Model(Status status, TweetModel model) {
		BeanCopyUtils.copyProperties(model, status);
		UserModel user = new UserModel();
		UserMapper.wbUser2Model(status.getUser(), user);
		model.setUser(user);
		if (status.getRetweetedStatus() != null) {
			ReasonModel reason = new ReasonModel();
			ReasonMapper.wbStatus2Model(status.getRetweetedStatus(), reason);
			model.setReason(reason);
		}
	}
}
