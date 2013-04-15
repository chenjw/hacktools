package com.chenjw.spider.dt.utils.transformer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

public class WbAtTransformer implements TextTransformer {

	public int startLength() {
		return 1;
	}

	public int endLength() {
		return 0;
	}

	public boolean isStart(char[] chars, int start) {
		return chars[start] == '@';
	}

	public int findEnd(char[] chars, int start) {
		for (int i = start; i < chars.length; i++) {
			if (chars[i] == ':' || chars[i] == ' ') {
				return i + 1;
			}
		}
		return chars.length;
	}

	public static void main(String[] args) {
		WbAtTransformer t = new WbAtTransformer();
		String post = t.transform("@aaaaa ");
		System.out.println(post);
	}

	@Override
	public String transform(String text) {
		String post = "";
		char p = text.charAt(text.length() - 1);

		String name = StringUtils.substringAfter(text, "@");
		if (p == ':' || p == ' ') {
			post += p;
			name = name.substring(0, name.length() - 1);
		}
		String out = "<a href=\"http://weibo.com/n/" + encode(name)
				+ "\" target=\"_blank\">@" + name + "</a>" + post;
		return out;
	}

	private String encode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
}
