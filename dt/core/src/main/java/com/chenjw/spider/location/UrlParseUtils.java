package com.chenjw.spider.location;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class UrlParseUtils {
	public static HttpUrl parseUrl(String url) {
		HttpUrl httpUrl = new HttpUrl();
		String address = null;
		Map<String, String> result = new HashMap<String, String>();
		if (!StringUtils.isBlank(url)) {

			if (url.indexOf("?") != -1) {
				address = StringUtils.substringBefore(url, "?");
				String params = StringUtils.substringAfter(url, "?");
				for (String keyValue : params.split("&")) {
					result.put(StringUtils.substringBefore(keyValue, "="),
							StringUtils.substringAfter(keyValue, "="));
				}
			}
			else{
				address = url;
			}
		}
		httpUrl.setUrl(address);
		httpUrl.setQueryParam(result);
		return httpUrl;
	}

	public static void main(String[] args) {
		String s = "allowForward=1&rootmid=3510010871085299&rootname=围脖小图库&rootuid=2806749204&rooturl=http://weibo.com/2806749204/z4cqz4ykP&url=http://weibo.com/1925238912/z4erLmIMy&mid=3510088495415362&name=陈俊文V&uid=1925238912&domain=xiaoxiaotuku&pid=a74b9814jw1dyncahj7r3j";
		for (Entry<String, String> e : parseUrl(s).getQueryParam().entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}
	}
}
