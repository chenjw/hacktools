package com.chenjw.spider.dt.listener;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import weibo4j.util.WeiboConfig;

import com.chenjw.spider.dt.constants.EnvConstants;

public class DtConfigListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		TimeZone tz = TimeZone.getTimeZone("ETC/GMT-8");
		TimeZone.setDefault(tz);
		if (EnvConstants.isProductMode()) {
			WeiboConfig.setProductMode();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
