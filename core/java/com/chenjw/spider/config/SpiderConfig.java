package com.chenjw.spider.config;

import java.util.Map;

public interface SpiderConfig {
	public PageConfig getPage(String pageId);

	public Map<String, PageConfig> getPages();

}
