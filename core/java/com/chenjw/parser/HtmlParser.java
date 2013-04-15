package com.chenjw.parser;

import java.util.Map;

public interface HtmlParser {
	/**
	 * 从html中读取需要的数据
	 * 
	 * @param html
	 * @return
	 */
	public Map<String, String> read(String html);

	/**
	 * 训练
	 * 
	 * @param template
	 */
	public void train(String template);
}
