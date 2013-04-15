package com.chenjw.spider.dt.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.util.Assert;

public class URLAccessObject {

	private Set<String> urlAccessObj;

	public boolean contains(String urlEscaped) {
		return urlAccessObj.contains(urlEscaped);
	}

	public URLAccessObject() {
		this.urlAccessObj = new HashSet<String>();
	}

	public void addUrlType(String urlEscaped) {
		Assert.notNull(urlEscaped);
		urlAccessObj.add(urlEscaped);
	}

}
