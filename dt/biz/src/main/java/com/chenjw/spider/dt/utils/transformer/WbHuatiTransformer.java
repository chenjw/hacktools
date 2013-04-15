package com.chenjw.spider.dt.utils.transformer;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

public class WbHuatiTransformer implements TextTransformer {

	public int startLength() {
		return 1;
	}

	public int endLength() {
		return 1;
	}

	public boolean isStart(char[] chars, int start) {
		return chars[start] == '#';
	}

	public int findEnd(char[] chars, int start) {
		for (int i = start; i < chars.length; i++) {
			if (chars[i] == '#') {
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		WbHuatiTransformer t = new WbHuatiTransformer();
		String post = t.transform("#aaaa#");
		System.out.println(post);
	}

	@Override
	public String transform(String text) {

		String name = StringUtils.substringBetween(text, "#", "#");
		return "<a class=\"a_topic\" href=\"http://huati.weibo.com/k/"
				+ encode(name) + "?from=501\" target=\"_blank\">" + text
				+ "</a>";
	}

	private String encode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

}
