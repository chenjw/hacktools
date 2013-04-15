package com.chenjw.spider;

import java.util.Map;

public interface Location {
	public String getPageId();

	public String getUrl();

	public Map<String, String> getQueryParam();

	public String getEncoding();

}