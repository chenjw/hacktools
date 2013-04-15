package com.chenjw.spider.dt.web.app.module.action;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class UserAction {
	@Autowired
	private UserService userService;
	@Autowired
	private WeiboService weiboService;

	private void oauth(String clientId, String clientSecret,Navigator navigator,HttpSession session) {
		// rundata.setTarget("oauth.vm");
		// pipelineContext.invokeNext();
		// 重定向到登录页面
		String redirectUrl = weiboService.findAuthorizeUrl(clientId);
		TokenModel tokenModel=new TokenModel();
		tokenModel.setClientId(clientId);
		tokenModel.setClientSecret(clientSecret);
		session.setAttribute(DtConstants.REGISTER_USER_SESSION_KEY, tokenModel);
		navigator.redirectToLocation(redirectUrl);
	}

	public void doCheckName(@Param(name = "screenName") String screenName,
			Navigator navigator, HttpSession session) {
		TokenModel tokenModel = userService
				.findWatchedUserByScreenName(screenName);
		if (tokenModel == null) {
			navigator.forwardTo("add.vm");
		} else {
			oauth(tokenModel.getClientId(),tokenModel.getClientSecret(), navigator,session);
		}
	}

	public void doAddUser(
			@Param(name = "clientId") String clientId,
			@Param(name = "clientSecret") String clientSecret, Context context,
			Navigator navigator, HttpSession session) {
		// 需要check

		oauth(clientId,clientSecret,navigator,session);
		//session.setAttribute(DtConstants.USER_SESSION_KEY, tokenModel);
		//session.setAttribute(DtConstants.LOGIN_USER_SESSION_KEY, tokenModel);
		// navigator.forwardTo("regOauth.vm");
		//navigator.redirectToLocation("/");
	}

}
