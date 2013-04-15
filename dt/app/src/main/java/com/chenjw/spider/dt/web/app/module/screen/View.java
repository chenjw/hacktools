package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.model.SearchInfo;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.Page;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.web.app.constants.DtConstants;
import com.chenjw.spider.dt.web.app.module.Base;

//3221292113
public class View extends Base {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;

	public void execute(@Param(name = "maxSort") String maxSort,
			Context context, Navigator navigator, HttpSession session) {

		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		checkAndView(userToken, maxSort, context, navigator, session);
		return;

	}

	private boolean allowDelete(HttpSession session, TokenModel userToken) {
		if (userToken == null) {
			return false;
		}
		TokenModel loginUserToken = (TokenModel) session
				.getAttribute(DtConstants.LOGIN_USER_SESSION_KEY);
		if (loginUserToken == null) {
			return false;
		}
		return StringUtils.equals(loginUserToken.getUserId(),
				userToken.getUserId());

	}

	private void checkAndView(TokenModel model, String maxSort,
			Context context, Navigator navigator, HttpSession session) {

		Page page = new Page();
		page.setPageSize(DtConstants.DEFAULT_PAGE_SIZE);
		page.setMaxSort(maxSort);

		SearchInfo searchInfo = findSearchInfo(session);
		PagedResult<TweetModel> result = deletedTweetReadService
				.findDeletedTweetsByUserId(searchInfo);
		context.put("detailList", result.getList());
		context.put("moreNum", result.getMoreNum());
		context.put("allowDelete", allowDelete(session, model));
		context.put("searchInfo", searchInfo);
		if (result.getList().size() > 0) {
			context.put("minSort",
					result.getList().get(result.getList().size() - 1)
							.getDeleteSort());
		}
		context.put(DtConstants.USER_SESSION_KEY,
				session.getAttribute(DtConstants.USER_SESSION_KEY));
		context.put(DtConstants.LOGIN_USER_SESSION_KEY,
				session.getAttribute(DtConstants.LOGIN_USER_SESSION_KEY));
		return;

	}

}