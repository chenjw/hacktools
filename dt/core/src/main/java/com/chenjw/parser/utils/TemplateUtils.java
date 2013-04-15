package com.chenjw.parser.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

public class TemplateUtils {
	private static final String START_TAG = "${";
	private static final String END_TAG = "}";

	public static Map<String, String> parse(String text, TemplateDesc desc) {
		text = initString(text);
		Object[] templates = desc.getDesc();
		Map<String, String> result = new HashMap<String, String>();
		if (templates == null || templates.length == 0) {
			return null;
		}
		int startIndex = 0;
		Tag currentTag = null;
		for (Object template : templates) {
			if (template instanceof Text) {
				Text tText = (Text) template;
				if (currentTag != null) {
					int index = text.indexOf(tText.getText(), startIndex);
					if (index == -1) {
						break;
					} else {
						String value = text.substring(startIndex, index);
						result.put(currentTag.getTagName(), value);
						currentTag = null;
						startIndex = index + tText.getText().length();
					}
				} else {
					if (text.startsWith(tText.getText(), startIndex)) {
						startIndex += tText.getText().length();
					} else {
						break;
					}
				}
			} else if (template instanceof Tag) {
				Tag tTag = (Tag) template;
				currentTag = tTag;
			}
		}
		if (currentTag != null) {
			String value = text.substring(startIndex);
			result.put(currentTag.getTagName(), value);
		}
		return result;
	}

	public static String render(String template, Map<String, String> params) {
		TemplateDesc desc = build(template);
		StringBuffer sb = new StringBuffer();
		for (Object obj : desc.getDesc()) {
			if (obj instanceof Tag) {
				String value = null;
				if (params != null) {
					value = params.get(((Tag) obj).getTagName());
				}
				sb.append(value);
			} else if (obj instanceof Text) {
				sb.append(((Text) obj).getText());
			} else {
				throw new RuntimeException("?????? why");
			}
		}
		return sb.toString();
	}

	public static TemplateDesc build(String template) {
		List<Object> desc = new ArrayList<Object>();
		if (StringUtils.isNotBlank(template)) {
			int i = 0;
			int startIndex = -1;
			for (;;) {
				startIndex = template.indexOf(START_TAG, i);
				if (startIndex == -1) {
					if (i < template.length()) {
						desc.add(new Text(initString(StringUtils.substring(
								template, i))));
					}
					break;
				} else {
					if (i < startIndex) {
						desc.add(new Text(initString(StringUtils.substring(
								template, i, startIndex))));
					}
				}
				int endIndex = template.indexOf(END_TAG, i);
				if (endIndex <= startIndex) {
					throw new java.lang.IllegalStateException(
							"endIndex<=startIndex, check key tag in "
									+ template);
				}
				String key = template.substring(
						startIndex + START_TAG.length(),
						endIndex - 1 + END_TAG.length());

				if (!StringUtils.isBlank(key)) {
					desc.add(new Tag(key));
				}
				i = endIndex + 1;
			}

		}
		TemplateDesc result = new TemplateDesc();
		result.setDesc(desc.toArray(new Object[desc.size()]));
		return result;
	}

	public static void main(String[] args) {
		Map<String, String> map = TemplateUtils.parse("11AA22CC33",
				TemplateUtils.build("11${aa}22${cc}33333"));
		for (Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " = " + entry.getValue());
		}
	}

	public static String initString(String str) {
		if (str == null) {
			return str;
		} else {
			return str.replaceAll("\r", "");
		}
	}

}
