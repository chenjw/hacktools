package com.chenjw.spider.dt.utils;

public class Page {
	private String maxSort;
	private String minSort;
	private int pageSize;

	public String getMinSort() {
		return minSort;
	}

	public void setMinSort(String minSort) {
		this.minSort = minSort;
	}

	public String getMaxSort() {
		return maxSort;
	}

	public void setMaxSort(String maxSort) {
		this.maxSort = maxSort;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
