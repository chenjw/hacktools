package com.chenjw.spider.dt.mapper;

import weibo4j.model.Status;

import com.chenjw.spider.dt.model.ReasonModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.tools.BeanCopyUtils;

public class ReasonMapper {

	public static void wbStatus2Model(Status status, ReasonModel model) {
		BeanCopyUtils.copyProperties(model, status);
		if (status.getUser() != null) {
			UserModel user = new UserModel();
			UserMapper.wbUser2Model(status.getUser(), user);
			model.setUser(user);
		}

	}
}
