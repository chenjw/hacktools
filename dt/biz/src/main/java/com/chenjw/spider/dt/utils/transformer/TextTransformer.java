package com.chenjw.spider.dt.utils.transformer;

public interface TextTransformer {
	public int startLength();

	public int endLength();

	public boolean isStart(char[] chars, int start);

	public int findEnd(char[] chars, int start);

	public String transform(String text);
}