package com.chenjw.spider.dt.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.parser.utils.TemplateUtils;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.location.HttpUrl;
import com.chenjw.spider.location.UrlParseUtils;

public class WapSpiderWeiboServiceImpl extends OpenWeiboServiceImpl {
	private HttpClient httpClient;

	// 个人时间轴
	String url2 = "http://weibo.cn/?since_id=ze6hnur3j&max_id=ze69UcZVq&prev_page=3&page=4";
	String cookie = "gsid_CTandWM=4ug5CpOz1GpTrZEsPBcbt84QeeI; _WEIBO_UID=${userId}";

	@Override
	public List<TweetModel> findUserTimelineByUserId(String userId,
			String token, long sinceId) {
		try {
			int page = 1;
			HttpUrl url = UrlParseUtils.parseUrl(url2);
			url.getQueryParam().put("userId", userId);
			url.getQueryParam().put("page", String.valueOf(page));
			String str = httpClient.get(url, "UTF-8",
					TemplateUtils.render(cookie, url.getQueryParam()));
			try {
				FileUtils.writeStringToFile(
						new File("/home/chenjw/test/a.txt"), str);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return new ArrayList<TweetModel>();
		} catch (HttpClientException e) {
			return null;
		}

	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
