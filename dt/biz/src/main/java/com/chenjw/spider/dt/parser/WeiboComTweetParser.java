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

public class WeiboComTweetParser {
	
	private static String filter(String text) {
		String r = "%%TESTTAG%%";
		text = StringUtils.replace(text, "</em>", r);
		text = StringUtils.replace(text, "</em", "");
		text = StringUtils.replace(text, r, "</em>");
		return text;
	}
	
	public static String parse(String text) {
		text=filter(text);
		Parser parser = Parser.createParser(text, null);

		WeiboComTweetNodeVisitor visitor = new WeiboComTweetNodeVisitor();
		try {
			parser.visitAllNodesWith(visitor);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return visitor.getTweet();
	}

	public static void main(String[] args) { 
		InputStream is = WeiboComTweetParser.class.getClassLoader()
				.getResourceAsStream(
						"com/chenjw/spider/dt/parser/weibo_com_tweet.html");
		try {
			String json = WeiboComTweetParser.parse(IOUtils.toString(is));

			System.out.println(json);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}

	}
}
