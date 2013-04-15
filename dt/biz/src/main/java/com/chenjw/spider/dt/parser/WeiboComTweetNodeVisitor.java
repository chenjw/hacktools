package com.chenjw.spider.dt.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Tag;
import org.htmlparser.visitors.NodeVisitor;

import com.alibaba.fastjson.JSON;
import com.chenjw.spider.dt.model.ReasonModel;
import com.chenjw.spider.dt.model.TweetModel;
import com.chenjw.spider.dt.model.UserModel;
import com.chenjw.spider.location.HttpUrl;
import com.chenjw.spider.location.UrlParseUtils;

public class WeiboComTweetNodeVisitor extends NodeVisitor {

	private String tweet;

	public String getTweet() {
		return tweet;
	}

	@Override
	public void visitTag(Tag tag) {
		String text = tag.toPlainTextString();

		if ("script".equalsIgnoreCase(tag.getTagName())
				&& StringUtils.startsWith(text,
						"STK && STK.pageletM && STK.pageletM.view(")) {
			String json = StringUtils.substringAfter(text,
					"STK && STK.pageletM && STK.pageletM.view(");
			json = StringUtils.substringBeforeLast(json, ")");
			Map<String, Object> map = null;
			try {
				map = JSON.parseObject(json, Map.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ("pl_content_weiboDetail".equals(map.get("pid"))) {
				tweet = map.get("html").toString();
			}
		}
	}

	@Override
	public void beginParsing() {

	}
}
