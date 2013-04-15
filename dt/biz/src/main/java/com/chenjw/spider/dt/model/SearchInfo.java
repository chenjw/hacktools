package com.chenjw.spider.dt.model;

import com.chenjw.spider.dt.constants.SearchTypeEnum;
import com.chenjw.spider.dt.utils.Page;

public class SearchInfo {
	private String senderUserId;
	private boolean useFollower = true;
	private boolean demo = false;
	private String followerUserId;
	private String senderUserName;
	private String followerUserName;

	private SearchTypeEnum type = SearchTypeEnum.TIMELINE;

	private Page page;

	public boolean supportDelete() {
		if (type == SearchTypeEnum.TOP_REPOSTS) {
			return false;
		}
		if (demo) {
			return false;
		}
		return true;
	}

	public String getSenderUserId() {
		return senderUserId;
	}

	public boolean isUseFollower() {
		return useFollower;
	}

	public void setUseFollower(boolean useFollower) {
		this.useFollower = useFollower;
	}

	public void setSenderUserId(String senderUserId) {
		this.senderUserId = senderUserId;
	}

	public String getFollowerUserId() {
		return followerUserId;
	}

	public void setFollowerUserId(String followerUserId) {
		this.followerUserId = followerUserId;
	}

	public SearchTypeEnum getType() {
		return type;
	}

	public void setType(SearchTypeEnum type) {
		this.type = type;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public boolean isDemo() {
		return demo;
	}

	public void setDemo(boolean demo) {
		this.demo = demo;
	}

	public String getSenderUserName() {
		return senderUserName;
	}

	public void setSenderUserName(String senderUserName) {
		this.senderUserName = senderUserName;
	}

	public String getFollowerUserName() {
		return followerUserName;
	}

	public void setFollowerUserName(String followerUserName) {
		this.followerUserName = followerUserName;
	}

}
