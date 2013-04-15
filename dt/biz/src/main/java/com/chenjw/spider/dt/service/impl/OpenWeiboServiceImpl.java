package com.chenjw.spider.dt.service.impl;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Account;
import weibo4j.Friendships;
import weibo4j.Oauth;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.http.ImageItem;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;

import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.mapper.UserMapper;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.dt.service.Handler;
import com.chenjw.spider.dt.service.WeiboService;

public class OpenWeiboServiceImpl implements WeiboService {
	// private String token = "2.00WaGSGC0TXPFW2859bb150etwDzcB";

	// private String DEFAULT_TOKEN = "2.00WaGSGCnpP1DBdf51a94343llFD7C";

	public String[] findFriendIdsByUserId(String userId, String token) {
		Friendships friendshipsManager = new Friendships();
		friendshipsManager.setToken(token);
		try {
			return friendshipsManager.getFriendsIdsByUid(userId, 5000, 0);
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void readUserTimeline(Timeline timelineManager, String uid,
			long sinceId, Handler handler) throws WeiboException {

		long lastId = 0;
		while (true) {
			Paging page = new Paging();
			if (lastId != 0) {
				page.setMaxId(lastId);
			}
			if (sinceId != 0) {
				// 因为需要包括这一条，所以必须-1
				page.setSinceId(sinceId - 1);
			}

			page.setCount(100);
			StatusWapper status;
			status = timelineManager.getUserTimelineByUid(uid, page, 0, 0);
			if (status.getStatuses().size() == 0) {
				return;
			}
			for (Status s : status.getStatuses()) {
				handler.handleStatus(s);
				lastId = s.getIdstr() - 1;
			}
			// 如果没到count数量，表示只有一页，退出
			if (status.getStatuses().size() < page.getCount()) {
				return;
			}

		}

	}

	private void readFrientsTimeline(Timeline timelineManager, long sinceId,
			Handler handler) throws WeiboException {
		long lastId = 0;

		while (true) {
			Paging page = new Paging();
			if (lastId != 0) {
				page.setMaxId(lastId);
			}
			if (sinceId != 0) {
				// 因为需要包括这一条，所以必须-1
				page.setSinceId(sinceId - 1);
			}
			System.out.println("[start-page] sinceId=" + page.getSinceId()
					+ ",maxId=" + page.getMaxId());
			page.setCount(100);
			StatusWapper status;
			status = timelineManager.getFriendsTimeline(0, 0, page);
			if (status.getStatuses().size() == 0) {
				return;
			}
			for (Status s : status.getStatuses()) {
				// System.out.println("[get-status] "
				// + s.getId()
				// + " "
				// + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				// .format(s.getCreatedAt()) + " ["
				// + s.getUser().getScreenName() + "] " + s.getText());
				handler.handleStatus(s);
				lastId = s.getIdstr() - 1;
			}
			System.out.println("[end-page] count="
					+ status.getStatuses().size());
			// 如果没到count数量，表示只有一页，退出
			if (status.getStatuses().size() < page.getCount()) {
				return;
			}

		}

	}

	public UserModel findUserByName(String name, String token) {
		Users usersManager = new Users();
		usersManager.setToken(token);
		User user;
		try {

			user = usersManager.showUserByScreenName(name);
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
		if (user == null) {
			return null;
		}
		UserModel u = new UserModel();
		UserMapper.wbUser2Model(user, u);
		return u;
	}

	public UserModel findUserByUserId(String userId, String token) {
		Users usersManager = new Users();
		usersManager.setToken(token);
		User user;
		try {
			user = usersManager.showUserById(userId);
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
		UserModel u = new UserModel();
		UserMapper.wbUser2Model(user, u);
		return u;
	}

	@Override
	public List<TweetModel> findUserTimelineByUserId(final String userId,
			String token, long sinceId) {
		Timeline timelineManager = new Timeline();
		timelineManager.setToken(token);
		final List<TweetModel> result = new ArrayList<TweetModel>();
		Handler handler = new Handler() {
			@Override
			public void handleStatus(Status s) {
				TweetModel model = new TweetModel();
				TweetMapper.wbStatus2Model(s, model);
				result.add(model);
			}
		};
		// 目标用户
		try {
			readUserTimeline(timelineManager, userId, sinceId, handler);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<TweetModel> findFriendsTimeline(TokenModel user, long sinceId) {
		System.out.println("findFriendsTimeline " + sinceId);
		Timeline timelineManager = new Timeline();
		timelineManager.setToken(user.getToken());
		final List<TweetModel> result = new ArrayList<TweetModel>();
		Handler handler = new Handler() {
			@Override
			public void handleStatus(Status s) {
				TweetModel model = new TweetModel();
				TweetMapper.wbStatus2Model(s, model);
				result.add(model);
			}
		};
		// 目标用户
		try {
			this.readFrientsTimeline(timelineManager, sinceId, handler);
		} catch (WeiboException e) {
			return null;
		}
		return result;
	}

	@Override
	public String findOriginStatusUrl(TokenModel user, String tid) {
		Timeline timelineManager = new Timeline();
		timelineManager.setToken(user.getToken());
		try {
			return timelineManager.QueryMid(1, tid).getString("mid");
			// http://weibo.com/1674758845/zdyUQ22Ba
		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String findUserIdByToken(String token) {
		Account account = new Account();
		account.setToken(token);
		String uid = null;
		try {
			uid = account.getUid().getString("uid");
		} catch (WeiboException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return uid;
	}

	public String findAuthorizeUrl() {
		Oauth oauth = new Oauth();
		try {
			return oauth.authorize("code", "", "");
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String findAuthorizeUrl(String clientId){
		Oauth oauth = new Oauth();
		try {

			return oauth.authorizeByClientId("code", clientId);
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String findAccessTokenByCode(String code,String clientId,String clientSecret) {
		Oauth oauth = new Oauth();

		// oauth.setToken(DEFAULT_TOKEN);
		try {
			return oauth.getAccessTokenByCode(code,clientId,clientSecret).getAccessToken();
		} catch (WeiboException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String parseSignedRequest(String signedRequest,String clientId,String clientSecret) {
		Oauth oauth = new Oauth();
		// oauth.setToken(DEFAULT_TOKEN);
		try {
			return oauth.parseSignedRequest(signedRequest);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public TweetModel upload(TokenModel user, String text, byte[] img)
			throws WeiboException {
		Timeline timeline = new Timeline();
		timeline.setToken(user.getToken());
		Status status = null;
		if (img == null) {
			status = timeline.UpdateStatus(text);
		} else {
			ImageItem imageItem = new ImageItem(img);
			status = timeline.UploadStatus(text, imageItem);
		}
		TweetModel model = new TweetModel();
		TweetMapper.wbStatus2Model(status, model);
		return model;
	}

	@Override
	public List<TweetModel> findTopTimeline() {
		throw new RuntimeException("not support");
	}
}
