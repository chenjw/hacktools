package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

//3221292113
/**
 * 演示页面
 * 
 * @author chenjw
 * 
 */
public class Demo {
	@Autowired
	private UserService userService;

	private void forceLogin(String force, HttpSession session) {
		if (!StringUtils.isBlank(force)) {
			TokenModel userToken = userService.findWatchedUserById(force);
			session.setAttribute(DtConstants.USER_SESSION_KEY, userToken);
		}
	}

	public void execute(Context context, Navigator navigator,
			HttpSession session) {
		String force = "3221292113";
		forceLogin(force, session);
		navigator.redirectToLocation("/");
		return;

	}

}