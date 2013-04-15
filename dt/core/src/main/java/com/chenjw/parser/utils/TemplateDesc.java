package com.chenjw.parser.utils;

import java.util.ArrayList;
import java.util.List;

public class TemplateDesc {
	private Object[] desc;

	public String[] getKeys() {
		List<String> result = new ArrayList<String>();
		for (Object o : desc) {
			if (o instanceof Tag) {
				result.add(((Tag) o).getTagName());
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

	public Object[] getDesc() {
		return desc;
	}

	public void setDesc(Object[] desc) {
		this.desc = desc;
	}

}