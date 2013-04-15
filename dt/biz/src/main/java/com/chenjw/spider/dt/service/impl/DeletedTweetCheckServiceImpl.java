package com.chenjw.spider.dt.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;

import weibo4j.util.WeiboConfig;

import com.chenjw.spider.dt.constants.Constants;
import com.chenjw.spider.dt.constants.EnvConstants;
import com.chenjw.spider.dt.dao.DeletedTweetDAO;
import com.chenjw.spider.dt.dao.TweetDAO;
import com.chenjw.spider.dt.dao.WatchedUserDAO;
import com.chenjw.spider.dt.dataobject.TweetDO;
import com.chenjw.spider.dt.dataobject.WatchedUserDO;
import com.chenjw.spider.dt.mapper.TokenMapper;
import com.chenjw.spider.dt.mapper.TweetMapper;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.dt.service.DeletedTweetCheckService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.utils.DeleteSortUtils;

public class DeletedTweetCheckServiceImpl implements DeletedTweetCheckService,
		InitializingBean {
	private WatchedUserDAO watchedUserDAO;
	private TweetDAO tweetDAO;
	private DeletedTweetDAO deletedTweetDAO;
	private WeiboService weiboService;
	private boolean running = false;

	private ExecutorService pool = new ThreadPoolExecutor(10, 10,
			Constants.WEIBO_QUERY_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public void start() {
		running = true;
	}

	public void stop() {
		running = false;
	}

	@Override
	public void checkByName(String name) {
		UserModel user = weiboService.findUserByName(name, null);
		if (user != null) {
			check(user.getId());
		}
	}

	private void checkWatchedUser(TokenModel user) {
		WeiboConfig.setClientInfo(user);
		Constants.LOGGER.info("start check " + user.getScreenName());
		// 当前时间
		Date currentDate = new Date();
		// 保留的时间
		Date retentionDate = DateUtils.addSeconds(currentDate,
				-Constants.TWEET_RETENTION_TIME);
		long minTid = 0;

		List<TweetDO> savedTweets = tweetDAO.findByMemberUserId(user
				.getUserId());
		//
		Set<String> retentionTweets = new HashSet<String>();
		for (TweetDO tweet : savedTweets) {
			// 如果已经过期
			if (retentionDate.after(tweet.getPostDate())) {
				// 删除
				tweetDAO.deleteByTidAndMemberUserId(tweet.getTid(),
						tweet.getMemberUserId());
			} else {
				retentionTweets.add(tweet.getTid());
				long longId = Long.parseLong(tweet.getTid());
				if (minTid == 0) {
					minTid = longId;
				} else {
					if (longId < minTid) {
						minTid = longId;
					}
				}
			}
		}
		TokenModel token = new TokenModel();
		token.setToken(user.getToken());
		token.setUserId(user.getUserId());
		List<TweetModel> newTweets = weiboService.findFriendsTimeline(token,
				minTid);
		if (newTweets == null) {
			return;
		}
		// 如果等于0表示一条都没取到，否则表示取到的最小ID
		long fetchedMinTid = 0;
		Set<String> ttttt = new HashSet<String>();
		for (TweetModel t : newTweets) {
			boolean bbbb = ttttt.add(t.getId());
			if (!bbbb) {
				System.out.println(t.getId() + " 重复了");
			}
			// 删除原来那条
			if (retentionTweets.contains(t.getId())) {
				retentionTweets.remove(t.getId());

			}
			if (!retentionDate.after(t.getCreatedAt())) {
				// 删除
				tweetDAO.deleteByTidAndMemberUserId(t.getId(), user.getUserId());
				TweetDO d = new TweetDO();
				TweetMapper.model2Do(t, d);
				d.setMemberUserId(user.getUserId());
				tweetDAO.addTweet(d);
			}

			long longId = Long.parseLong(t.getId());
			if (fetchedMinTid == 0) {
				fetchedMinTid = longId;
			} else {
				if (longId < fetchedMinTid) {
					fetchedMinTid = longId;
				}
			}
		}
		for (String tid : retentionTweets) {
			TweetDO s = deletedTweetDAO.findByTidAndMemberUserId(tid,
					user.getUserId());
			if (s == null) {
				TweetDO deleted = tweetDAO.findByTidAndMemberUserId(tid,
						user.getUserId());
				if (fetchedMinTid != 0
						&& Long.parseLong(deleted.getTid()) > fetchedMinTid) {
					deleted.setDeleteDate(currentDate);
					deleted.setDeleteSort(DeleteSortUtils.getDeleteSort(
							currentDate, deleted.getTid()));
					deletedTweetDAO.addTweet(deleted);
				}
			}
		}
		Constants.LOGGER.info("end check " + user.getScreenName());
	}

	@Override
	public void check(String userId) {
		WatchedUserDO user = watchedUserDAO.findWatchedUser(userId);
		if (user != null) {
			TokenModel model = new TokenModel();
			TokenMapper.do2Model(user, model);
			checkWatchedUser(model);
		}

	}

	@Override
	public void checkAll() {
		int instanceIndex = EnvConstants.getEnvProvider().getInstanceIndex();
		int instanceCount = EnvConstants.getEnvProvider().getInstanceCount();
		List<WatchedUserDO> users = watchedUserDAO.getAllWatchedUsers();
		for (final WatchedUserDO user : users) {
			Long userId = Long.parseLong(user.getUserId());
			// 根据userId最后一位来分配
			if ((userId % instanceCount) != instanceIndex) {
				continue;
			}
			final TokenModel model = new TokenModel();
			TokenMapper.do2Model(user, model);
			
			if (model.isValid() && !StringUtils.isBlank(model.getToken())) {
				pool.execute(new Runnable() {
					@Override
					public void run() {
						checkWatchedUser(model);
					}
				});
			}

		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// if (EnvConstants.isProductMode()) {
		start();
		// }
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (running) {
						try {
							DeletedTweetCheckServiceImpl.this.checkAll();
							Constants.LOGGER.info("checkAll finished!");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(Constants.WEIBO_QUERY_TIME);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}

		}).start();
	}

	public void setWatchedUserDAO(WatchedUserDAO watchedUserDAO) {
		this.watchedUserDAO = watchedUserDAO;
	}

	public void setTweetDAO(TweetDAO tweetDAO) {
		this.tweetDAO = tweetDAO;
	}

	public void setDeletedTweetDAO(DeletedTweetDAO deletedTweetDAO) {
		this.deletedTweetDAO = deletedTweetDAO;
	}

	public void setWeiboService(WeiboService weiboService) {
		this.weiboService = weiboService;
	}

}
