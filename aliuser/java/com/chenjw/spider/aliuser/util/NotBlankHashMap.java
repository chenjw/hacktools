package com.chenjw.spider.aliuser.util;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

public class NotBlankHashMap<K, V> extends HashMap<K, V> {

	private static final long serialVersionUID = 5290112813826480478L;

	@Override
	public V put(K key, V value) {
		if (key == null || value == null) {
			return null;
		} else if (StringUtils.isBlank(value.toString())) {
			return null;
		}
		return super.put(key, value);
	}

}
