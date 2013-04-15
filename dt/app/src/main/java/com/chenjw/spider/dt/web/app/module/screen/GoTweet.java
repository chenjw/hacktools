package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class GoTweet {
	@Autowired
	private WeiboService weiboService;

	public void execute(@Param(name = "tid") String tid, Context context,
			Navigator navigator, HttpSession session) {

		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		String mid = weiboService.findOriginStatusUrl(userToken, tid);
		String url = "http://weibo.com/" + userToken.getUserId() + "/" + mid;
		System.out.println("[go-tweet] " + url);
		navigator.redirectToLocation(url);
	}
}
