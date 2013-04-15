package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.constants.SearchTypeEnum;
import com.chenjw.spider.dt.model.SearchInfo;
import com.chenjw.spider.dt.service.DeletedTweetReadService;
import com.chenjw.spider.dt.web.app.module.Base;

/**
 * 查找最新的微博
 * 
 * @author chenjw
 * 
 */
public class CountNew extends Base {
	@Autowired
	private DeletedTweetReadService deletedTweetReadService;

	public void execute(@Param(name = "minSort") String minSort,
			Context context, Navigator navigator, HttpSession session) {

		SearchInfo searchInfo = findSearchInfo(session);
		if (searchInfo.getType() == SearchTypeEnum.COUNT_NEW) {
			int count = deletedTweetReadService
					.countDeletedTweetsByUserId(searchInfo);
			context.put("count", count);
		}
		return;
	}

}