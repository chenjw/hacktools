package com.chenjw.spider.dt.model;

import java.util.Date;

public class TweetModel implements java.io.Serializable {

	private static final long serialVersionUID = -8795691786466526420L;
	private String html;

	private UserModel user; // 作者信息
	private Date createdAt; // status创建时间
	private String id; // status id
	private String mid; // 微博MID
	private String text; // 微博内容

	private String thumbnailPic; // 微博内容中的图片的缩略地址
	private String bmiddlePic; // 中型图片
	private String originalPic; // 原始图片

	private int repostsCount; // 转发数
	private int commentsCount; // 评论数

	private ReasonModel reason;

	//
	// 删除时间
	private Date deleteDate;
	// 删除排序
	private String deleteSort;

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getThumbnailPic() {
		return thumbnailPic;
	}

	public void setThumbnailPic(String thumbnailPic) {
		this.thumbnailPic = thumbnailPic;
	}

	public String getBmiddlePic() {
		return bmiddlePic;
	}

	public void setBmiddlePic(String bmiddlePic) {
		this.bmiddlePic = bmiddlePic;
	}

	public String getOriginalPic() {
		return originalPic;
	}

	public void setOriginalPic(String originalPic) {
		this.originalPic = originalPic;
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

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public ReasonModel getReason() {
		return reason;
	}

	public void setReason(ReasonModel reason) {
		this.reason = reason;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
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

}
