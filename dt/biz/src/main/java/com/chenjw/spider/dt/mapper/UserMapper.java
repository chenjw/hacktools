package com.chenjw.spider.dt.mapper;

import weibo4j.model.User;

import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.tools.BeanCopyUtils;

public class UserMapper {

	public static void wbUser2Model(User user, UserModel model) {
		BeanCopyUtils.copyProperties(model, user);
	}

}
