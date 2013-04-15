package com.chenjw.spider.dt.dataobject;

import java.util.Date;

public class TweetDO {

	private String tid;
	// 微博发送者的id
	private String userId;
	// 微博关注者的id
	private String memberUserId;
	// 发送时间
	private Date postDate;
	// 内容
	private byte[] content;
	// 删除时间
	private Date deleteDate;
	// 删除排序
	private String deleteSort;
	// 转发数
	private int repostsCount;
	// 评论数
	private int commentsCount;

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMemberUserId() {
		return memberUserId;
	}

	public void setMemberUserId(String memberUserId) {
		this.memberUserId = memberUserId;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public String getDeleteSort() {
		return deleteSort;
	}

	public void setDeleteSort(String deleteSort) {
		this.deleteSort = deleteSort;
	}

	public int getRepostsCount() {
		return repostsCount;
	}

	public void setRepostsCount(int repostsCount) {
		this.repostsCount = repostsCount;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

}
