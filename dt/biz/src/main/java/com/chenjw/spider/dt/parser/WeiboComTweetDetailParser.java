package com.chenjw.spider.dt.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;
import com.chenjw.spider.dt.model.TweetModel;

public class WeiboComTweetDetailParser {

	public static void parse(TweetModel tweet, String text) {
		Parser parser = Parser.createParser(text, null);
		WeiboComTweetDetailNodeVisitor visitor = new WeiboComTweetDetailNodeVisitor(
				tweet);
		try {
			parser.visitAllNodesWith(visitor);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		InputStream is = WeiboComTweetDetailParser.class
				.getClassLoader()
				.getResourceAsStream(
						"com/chenjw/spider/dt/parser/weibo_com_tweet_detail.html");
		try {
			TweetModel tweet = new TweetModel();
			WeiboComTweetDetailParser.parse(tweet, IOUtils.toString(is));
			System.out.println(tweet);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}

	}
}
