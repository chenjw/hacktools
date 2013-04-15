package com.chenjw.spider.dt.constants;

import java.util.logging.Logger;

import weibo4j.http.HttpClient;

public class Constants {
	// 微博保留的时间间隔（单位秒），默认保留4天内的所有微博。
	public static int TWEET_RETENTION_TIME = 4 * 24 * 60 * 60;
	// 查找微博，轮询时间间隔
	public static int WEIBO_QUERY_TIME = 2 * 60 * 1000;

	// 查找微博，超时时间
	public static int WEIBO_QUERY_KEEP_ALIVE_TIME = 90 * 1000;
	public static final Logger LOGGER = Logger.getLogger(HttpClient.class
			.getName());
}
