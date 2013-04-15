package com.chenjw.spider.dt.constants;

public enum SearchTypeEnum {

	COUNT_NEW, TIMELINE, TOP_REPOSTS;

	public static SearchTypeEnum parse(String type) {
		for (SearchTypeEnum s : SearchTypeEnum.values()) {
			if (s.name().equals(type)) {
				return s;
			}
		}
		return TIMELINE;
	}
}
