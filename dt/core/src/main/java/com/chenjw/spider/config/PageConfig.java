package com.chenjw.spider.config;

import java.util.Map;

public interface PageConfig {
	public String getUrl();

	public Map<String, String> getParams();

	public String getEncoding();

	public String[] getTrainHtmls();
}
