package com.chenjw.spider.dt.web.app.module.screen;

import weibo4j.util.WeiboConfig;

import com.alibaba.citrus.turbine.Context;

//3221292113
/**
 * 
 * @author chenjw
 * 
 */
public class Oauth {

	public void execute(Context context) {
		// redirect_URI
		// client_ID
		context.put("client_ID", WeiboConfig.getValue("client_ID"));
		context.put("app_URI", WeiboConfig.getValue("app_URI"));
	}
}