package com.chenjw.spider.config.impl;

import java.util.Map;

import com.chenjw.spider.config.PageConfig;


public class SimplePageConfig implements PageConfig {
	private String url;
	private Map<String, String> params;
	private String encoding;
	private String[] trainHtmls;

	public String getUrl() {
		return url;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getEncoding() {
		return encoding;
	}

	public String[] getTrainHtmls() {
		return trainHtmls;
	}

	protected void setUrl(String url) {
		this.url = url;
	}

	protected void setParams(Map<String, String> params) {
		this.params = params;
	}

	protected void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	protected void setTrainHtmls(String[] trainHtmls) {
		this.trainHtmls = trainHtmls;
	}

}
