package com.chenjw.spider.dt.utils;

import com.chenjw.spider.dt.utils.transformer.TextTransformer;
import com.chenjw.spider.dt.utils.transformer.WbAtTransformer;
import com.chenjw.spider.dt.utils.transformer.WbHttpLinkTransformer;
import com.chenjw.spider.dt.utils.transformer.WbHuatiTransformer;

public class WbTextUtils {
	private static TextTransformer[] transformers = new TextTransformer[] {
			new WbAtTransformer(), new WbHttpLinkTransformer(),
			new WbHuatiTransformer() };

	public static String toWbText(String text) {
		if (text == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		char[] chars = text.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			boolean matched = false;
			for (TextTransformer transformer : transformers) {
				int start = i;
				if (transformer.isStart(chars, start)) {
					start += transformer.startLength();
					start = transformer.findEnd(chars, start);
					if (start != -1) {
						start += transformer.endLength();

						String t = new String(chars, i, start - i);
						String transd = transformer.transform(t);
						sb.append(transd);
						matched = true;
						i = start - 1;
						break;
					}
				}
			}
			if (!matched) {
				sb.append(chars[i]);
			}
		}

		return sb.toString();
	}

	public static void main(String args[]) {
		String str = "aaaa http://t.cn/zjsizRda ";
		System.out.println(str);
		String r = WbTextUtils.toWbText(str);
		System.out.println(r);
	}
}
