package com.chenjw.spider.dt.utils.transformer;

public class WbHttpLinkTransformer implements TextTransformer {
	private static final String START = "http://t.cn/";

	public int startLength() {
		return START.length();
	}

	public int endLength() {
		return 0;
	}

	public boolean isStart(char[] chars, int start) {
		if (start + START.length() > chars.length) {
			return false;
		}
		return START.equals(new String(chars, start, START.length()));
	}

	public int findEnd(char[] chars, int start) {
		int l = start + 7;
		if (l > chars.length) {
			return -1;
		}
		return l;
	}

	public static void main(String[] args) {
		WbHttpLinkTransformer t = new WbHttpLinkTransformer();
		String post = t.transform("http://t.cn/zjsclg5");
		System.out.println(post);
	}

	@Override
	public String transform(String text) {
		return "<a title=\"" + text + "\" href=\"" + text
				+ "\" target=\"_blank\">" + text + "</a>";
	}

}
