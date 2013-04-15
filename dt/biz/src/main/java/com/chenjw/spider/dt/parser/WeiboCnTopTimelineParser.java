package com.chenjw.spider.dt.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import com.alibaba.fastjson.JSON;
import com.chenjw.spider.dt.model.TweetModel;

public class WeiboCnTopTimelineParser {
	public static List<TweetModel> parse(String text) {
		Parser parser = Parser.createParser(text, null);

		WeiboCnTopTimelineNodeVisitor visitor = new WeiboCnTopTimelineNodeVisitor();
		try {
			parser.visitAllNodesWith(visitor);
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return visitor.getTweets();
	}

	public static void main(String[] args) {
		InputStream is = WeiboCnTopTimelineParser.class.getClassLoader()
				.getResourceAsStream("com/chenjw/spider/dt/parser/weibo_cn_top_timeline.html");
		try {
			List<TweetModel> list = WeiboCnTopTimelineParser.parse(IOUtils
					.toString(is));
			for (TweetModel m : list) {
				System.out.println(JSON.toJSONString(m));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(is);
		}

	}
}
