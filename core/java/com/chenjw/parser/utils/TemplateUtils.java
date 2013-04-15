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

	public static class TemplateDesc {
		private Object[] desc;

		public String[] getKeys() {
			List<String> result = new ArrayList<String>();
			for (Object o : desc) {
				if (o instanceof Tag) {
					result.add(((Tag) o).tagName);
				}
			}
			return result.toArray(new String[result.size()]);
		}

		public boolean isTemplate() {
			for (Object o : desc) {
				if (o instanceof Tag) {
					return true;
				}
			}
			return false;
		}

	}

	public static Map<String, String> parse(String text, TemplateDesc desc) {
		text = initString(text);
		Object[] templates = desc.desc;
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
					int index = text.indexOf(tText.text, startIndex);
					if (index == -1) {
						break;
					} else {
						String value = text.substring(startIndex, index);
						result.put(currentTag.tagName, value);
						currentTag = null;
						startIndex = index + tText.text.length();
					}
				} else {
					if (text.startsWith(tText.text, startIndex)) {
						startIndex += tText.text.length();
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
			result.put(currentTag.tagName, value);
		}
		return result;
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
		result.desc = desc.toArray(new Object[desc.size()]);
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

	public static class Tag {
		String tagName;

		public Tag(String tagName) {
			this.tagName = tagName;
		}

		@Override
		public String toString() {
			return tagName;
		}

	}

	public static class Text {
		String text;

		public Text(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}

	}
}
