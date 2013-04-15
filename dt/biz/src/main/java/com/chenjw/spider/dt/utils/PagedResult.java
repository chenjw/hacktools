package com.chenjw.spider.dt.utils;

import java.util.List;

public class PagedResult<T> {
	private List<T> list;

	private int moreNum;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public int getMoreNum() {
		return moreNum;
	}

	public void setMoreNum(int moreNum) {
		this.moreNum = moreNum;
	}

}
