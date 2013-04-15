package com.chenjw.spider.weibo;

import weibo4j.model.Comment;
import weibo4j.model.Status;

public interface Handler {
	public void handleStatus(Status s);

	public void handleComment(Status s, Comment c);
}
