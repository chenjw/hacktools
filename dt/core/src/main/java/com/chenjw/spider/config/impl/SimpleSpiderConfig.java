package com.chenjw.spider.config.impl;

import java.util.HashMap;
import java.util.Map;

import com.chenjw.spider.config.PageConfig;
import com.chenjw.spider.config.SpiderConfig;


public class SimpleSpiderConfig implements SpiderConfig {

	private Map<String, PageConfig> pages = new HashMap<String, PageConfig>();

	@Override
	public PageConfig getPage(String pageId) {
		return pages.get(pageId);
	}

	public Map<String, PageConfig> getPages() {
		return pages;
	}

	public void addPage(String pageId, PageConfig pageConfig) {
		pages.put(pageId, pageConfig);
	}

}
