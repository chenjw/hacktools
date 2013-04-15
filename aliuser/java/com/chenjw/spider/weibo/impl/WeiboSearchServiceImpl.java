package com.chenjw.spider.weibo.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;

import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

import com.chenjw.spider.weibo.Handler;
import com.chenjw.spider.weibo.WeiboSearchService;

public class WeiboSearchServiceImpl implements WeiboSearchService {
	private String token = "2.00WaGSGC0TXPFW2859bb150etwDzcB";
	private Timeline timelineManager = new Timeline();
	private Comments commentsManager = new Comments();
	private Users usersManager = new Users();
	private Friendships friendshipsManager = new Friendships();
	{
		timelineManager.setToken(token);
		commentsManager.setToken(token);
		usersManager.setToken(token);
		friendshipsManager.setToken(token);

	}

	private void readComments(Status s, Handler handler) throws WeiboException {
		if (s.getCommentsCount() == 0) {
			return;
		}
		long lastId = -1;
		while (true) {
			Paging page = new Paging();
			if (lastId != -1) {
				page.setMaxId(lastId);
			}
			page.setCount(50);
			CommentWapper comments;

			comments = commentsManager.getCommentById(s.getId(), page, 0);
			if (comments.getComments().size() == 0) {
				return;
			}
			for (Comment c : comments.getComments()) {
				handler.handleComment(s, c);
				lastId = c.getId() - 1;
			}

		}

	}

	// 潇潇coffelife

	private void readStatuses(String uid, Handler handler)
			throws WeiboException {
		long lastId = -1;
		while (true) {

			Paging page = new Paging();
			if (lastId != -1) {
				page.setMaxId(lastId);
			}
			page.setCount(100);
			StatusWapper status;

			status = timelineManager.getUserTimelineByUid(uid, page, 0, 0);
			if (status.getStatuses().size() == 0) {
				return;
			}
			for (Status s : status.getStatuses()) {
				handler.handleStatus(s);
				readComments(s, handler);
				lastId = s.getIdstr() - 1;
			}

		}

	}

	private void readFriends(String uid, Handler handler) throws WeiboException {

		int cursor = -1;
		int count = 200;
		while (true) {
			UserWapper user;

			user = friendshipsManager.getFriendsByID(uid, count, cursor);
			if (user.getUsers().size() == 0) {
				return;
			}
			for (User u : user.getUsers()) {
				readStatuses(u.getId(), handler);
			}
			cursor = (int) user.getNextCursor();
		}

	}

	public static void main(String[] args) throws Exception {
		WeiboSearchServiceImpl t = new WeiboSearchServiceImpl();
		t.view("潇潇coffelife");
	}

	@Override
	public void view(final String name) throws Exception {
		final String sep = "[S_E_P]";
		File f = new File("/home/chenjw/test/weibo.txt");
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(
				new FileOutputStream(f), "UTF-8"));

		final User user = usersManager.showUserByScreenName(name);
		Handler handler = new Handler() {

			@Override
			public void handleStatus(Status s) {
				// 不相关
				if (!StringUtils.equals(s.getUser().getId(), user.getId())) {
					if (s.getText().indexOf("@" + name) == -1) {
						return;
					}
				}
				pw.println("[STATUS]" + s.getText() + sep
						+ s.getCommentsCount() + sep + s.getOriginalPic() + sep
						+ s.getCreatedAt() + sep + s.getId());
				System.out.println("[STATUS]" + s.getText());
				pw.flush();
			}

			@Override
			public void handleComment(Status s, Comment c) {
				// 不相关
				if (!StringUtils.equals(c.getUser().getId(), user.getId())) {
					if (c.getText().indexOf("@" + name) == -1) {
						return;
					}
				}
				pw.println("[COMMENT]"
						+ c.getText()
						+ sep
						+ s.getId()
						+ sep
						+ c.getId()
						+ sep
						+ (c.getReplycomment() == null ? "" : c
								.getReplycomment().getId()));

				System.out.println("[COMMENT]" + c.getText());
				pw.flush();
			}

		};
		String uid = user.getId();
		// 目标用户
		readStatuses(uid, handler);
		// 目标用户的关注用户
		readFriends(uid, handler);
		System.out.println("finished!");
	}
}
