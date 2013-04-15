package com.chenjw.spider.dt.web.app.module.screen;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.dt.service.DeletedTweetCheckService;

public class Stop {
	@Autowired
	private DeletedTweetCheckService deletedTweetService;

	public void execute(Context context, Navigator navigator) {
		deletedTweetService.stop();
		context.put("message", "已停止抓取任务！");
		navigator.forwardTo("message.vm");
	}

}
