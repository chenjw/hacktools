package com.chenjw.parser.rule;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Tag;

import com.chenjw.parser.Rule;

public class IdRule implements Rule {

	@Override
	public int isLike(Node baseNode, Node newNode) {
		if (getId(baseNode) == null) {
			if (getId(newNode) == null) {
				return 0;
			} else {
				return -1;
			}
		} else {
			if (getId(newNode) == null) {
				return -1;
			} else {
				if (StringUtils.equals(getId(baseNode), getId(newNode))) {
					return 1;
				} else {
					return -1;
				}
			}
		}
	}

	private String getId(Node node) {
		if (node instanceof Tag) {
			Tag tag = (Tag) node;
			return tag.getAttribute("id");
		}
		return null;
	}
}
