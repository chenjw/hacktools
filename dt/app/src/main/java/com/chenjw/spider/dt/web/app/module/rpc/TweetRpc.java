/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.spider.dt.web.app.module.rpc;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;
import com.alibaba.citrus.service.template.TemplateException;
import com.alibaba.citrus.service.template.TemplateService;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.turbine.support.ContextAdapter;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.alibaba.citrus.webx.WebxException;
import com.alibaba.nonda.databind.annotation.RequestParam;
import com.chenjw.spider.dt.constants.SearchTypeEnum;
import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.model.SearchInfo;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.utils.PagedResult;
import com.chenjw.spider.dt.utils.Result;
import com.chenjw.spider.dt.web.app.constants.DtConstants;
import com.chenjw.spider.dt.web.app.module.Base;

public class TweetRpc extends Base {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;
	@Autowired
	private DeletedTweetDAO deletedTweetDAO;
	@Autowired
	private TemplateService templateService;

	@ResourceMapping
	public Map<String, Object> deleteTweet(
			@RequestParam(name = "tid") String tid, HttpSession session) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isBlank(tid)) {
			result.put("errorCode", "tid is null");
			result.put("success", false);
			return result;
		}
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		deletedTweetDAO.deleteByTidAndMemberUserId(tid, userToken.getUserId());
		result.put("success", true);
		return result;
	}

	@ResourceMapping
	public Map<String, Object> page(HttpSession session) {
		SearchInfo searchInfo = findSearchInfo(session);
		if (searchInfo.getType() == SearchTypeEnum.TIMELINE) {
			return timeLine(searchInfo, session);
		} else if (searchInfo.getType() == SearchTypeEnum.TOP_REPOSTS) {
			return topReposts(searchInfo, session);
		} else if (searchInfo.getType() == SearchTypeEnum.COUNT_NEW) {
			return countNew(searchInfo, session);
		} else {
			return null;
		}
	}

	private Map<String, Object> countNew(SearchInfo searchInfo,
			HttpSession session) {
		HashMap<String, Object> r = new HashMap<String, Object>();
		TurbineRunDataInternal runData = (TurbineRunDataInternal) TurbineUtil
				.getTurbineRunData(request);
		Context context = runData.getContext();
		int count = deletedTweetReadService
				.countDeletedTweetsByUserId(searchInfo);

		context.put("count", count);
		String pageHtml = "";
		try {
			pageHtml = render("control/countNew.vm", context);
		} catch (Exception e) {
			throw new WebxException(e);
		}
		r.put("page", pageHtml);
		r.put("success", true);
		return r;
	}

	private Map<String, Object> timeLine(SearchInfo searchInfo,
			HttpSession session) {
		TurbineRunDataInternal runData = (TurbineRunDataInternal) TurbineUtil
				.getTurbineRunData(request);
		Context context = runData.getContext();
		HashMap<String, Object> r = new HashMap<String, Object>();

		PagedResult<TweetModel> result = deletedTweetReadService
				.findDeletedTweetsByUserId(searchInfo);

		context.put("detailList", result.getList());
		context.put("moreNum", result.getMoreNum());
		context.put("searchInfo", searchInfo);
		String maxSort = "0";
		if (result.getList().size() > 0) {
			context.put("minSort",
					result.getList().get(result.getList().size() - 1)
							.getDeleteSort());
			maxSort = result.getList().get(0).getDeleteSort();
		}

		String pageHtml = "";
		try {
			pageHtml = render("control/detailList.vm", context);
		} catch (Exception e) {
			throw new WebxException(e);
		}
		r.put("page", pageHtml);
		r.put("maxSort", maxSort);
		r.put("success", true);
		r.put("searchInfo", searchInfo);
		r.put("nav", navHtml(searchInfo, context, session));
		return r;

	}

	private Map<String, Object> topReposts(SearchInfo searchInfo,
			HttpSession session) {
		TurbineRunDataInternal runData = (TurbineRunDataInternal) TurbineUtil
				.getTurbineRunData(request);
		Context context = runData.getContext();
		HashMap<String, Object> r = new HashMap<String, Object>();

		Result<TweetModel> result = deletedTweetReadService
				.findTopReposts(searchInfo);
		context.put("detailList", result.getList());
		context.put("moreNum", -1);
		context.put("allowDelete", false);
		String pageHtml = "";
		try {
			pageHtml = render("control/detailList.vm", context);
		} catch (Exception e) {
			throw new WebxException(e);
		}
		r.put("page", pageHtml);
		r.put("success", true);
		r.put("searchInfo", searchInfo);
		r.put("nav", navHtml(searchInfo, context, session));
		return r;

	}

	private String render(String target, Context context) {
		StringWriter sw = new StringWriter();
		try {
			templateService.writeTo(target, new ContextAdapter(context), sw);
		} catch (TemplateException e) {
			throw new WebxException(e);
		} catch (IOException e) {
			throw new WebxException(e);
		}
		return sw.toString();
	}

	private String navHtml(SearchInfo searchInfo, Context context,
			HttpSession session) {
		context.put("searchInfo", searchInfo);
		context.put(DtConstants.LOGIN_USER_SESSION_KEY,
				session.getAttribute(DtConstants.LOGIN_USER_SESSION_KEY));
		String pageHtml = "";
		try {
			pageHtml = render("control/nav.vm", context);
		} catch (Exception e) {
			throw new WebxException(e);
		}
		return pageHtml;
	}

}
