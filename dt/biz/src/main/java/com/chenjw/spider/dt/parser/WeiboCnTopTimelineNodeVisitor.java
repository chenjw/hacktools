package com.chenjw.spider.dt.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Tag;
import org.htmlparser.visitors.NodeVisitor;

import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.location.HttpUrl;
import com.chenjw.spider.location.UrlParseUtils;

public class WeiboCnTopTimelineNodeVisitor extends NodeVisitor {

	private List<TweetModel> tweets = new ArrayList<TweetModel>();

	public List<TweetModel> getTweets() {
		return tweets;
	}

	@Override
	public void visitTag(Tag tag) {

		//class="c" id="M_zjV4rFIg6"
		//<a href="http://weibo.cn/repost/zjW5wviaG?uid=1642088277&rl=0">转发[38820]</a>
		if ("a".equalsIgnoreCase(tag.getTagName()) && StringUtils.startsWith(tag.toPlainTextString(), "转发") &&  StringUtils.startsWith(tag.getAttribute("href"), "http://weibo.cn/repost/")) {
			TweetModel tweet = new TweetModel();
			tweet.setUser(new UserModel());
			String s = tag.getAttribute("href");
			HttpUrl parseUrl = UrlParseUtils.parseUrl(s);
			tweet.getUser().setId(parseUrl.getQueryParam().get("uid"));
			tweet.setMid(StringUtils.substringAfter(parseUrl.getUrl(), "http://weibo.cn/repost/"));
			tweets.add(tweet);
		} 
	}

	@Override
	public void beginParsing() {
		tweets = new ArrayList<TweetModel>();
	}
}
