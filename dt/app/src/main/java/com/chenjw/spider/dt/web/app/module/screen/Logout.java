package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.constants.UserStatusEnum;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.UserService;

//3221292113
public class Logout {
	@Autowired
	private UserService userService;

	public void execute(@Param(name = "source") String source,
			@Param(name = "uid") String uid,
			@Param(name = "auth_end") String authEnd, Context context,
			Navigator navigator, HttpSession session) {
		TokenModel token = userService.findWatchedUserById(uid);
		if (token != null) {
			token.setStatus(UserStatusEnum.INVALID);
		}
		userService.updateUser(token);
		navigator.redirectToLocation("/");
		return;
	}
}