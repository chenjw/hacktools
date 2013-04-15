package com.chenjw.spider.location;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parser.utils.TemplateUtils;

public class HttpUrl {
	private String url;
	private Map<String, String> queryParam;

	public HttpUrl() {

	}

	public HttpUrl(String url, Map<String, String> queryParam) {
		this.url = url;
		this.queryParam = queryParam;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getQueryParam() {
		return queryParam;
	}

	public void setQueryParam(Map<String, String> queryParam) {
		this.queryParam = queryParam;
	}

	public String getRenderedUrl() {
		return TemplateUtils.render(this.url, queryParam);
	}

	public String toUrlString() {
		return toUrlString("UTF-8");
	}

	protected String toUrlString(String encoding) {
		String temp = getRenderedUrl();
		boolean isUrlHasParam = temp.indexOf("?") != -1;
		StringBuffer sb = new StringBuffer(temp);
		if (queryParam != null) {
			boolean isFirst = true;
			for (Entry<String, String> entry : queryParam.entrySet()) {
				if (!StringUtils.isBlank(entry.getValue())) {
					if (!isUrlHasParam && isFirst) {
						sb.append('?');
						isFirst = false;
					} else {
						sb.append('&');
					}
					try {
						sb.append(entry.getKey())
								.append('=')
								.append(URLEncoder.encode(entry.getValue(),
										encoding));
					} catch (UnsupportedEncodingException e) {
						//
					}
				}
			}
		}
		return sb.toString();
	}

}
