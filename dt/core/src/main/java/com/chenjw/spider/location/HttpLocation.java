package com.chenjw.spider.location;


public class HttpLocation extends HttpUrl {

	private String encoding;

	private String pageId;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getPageId() {
		return pageId;
	}

	public void setPageId(String pageId) {
		this.pageId = pageId;
	}

	public String toUrlString() {
		if (encoding == null) {
			return toUrlString("UTF-8");
		} else {
			return toUrlString(encoding);
		}
	}

}