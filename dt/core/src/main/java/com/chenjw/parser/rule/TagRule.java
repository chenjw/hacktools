package com.chenjw.parser.rule;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Tag;

import com.chenjw.parser.Rule;

public class TagRule implements Rule {

	@Override
	public int isLike(Node baseNode, Node newNode) {
		if ((baseNode instanceof Tag) && (newNode instanceof Tag)) {
			Tag baseTag = (Tag) baseNode;
			Tag newTag = (Tag) newNode;
			if (StringUtils.equals(baseTag.getTagName(), newTag.getTagName())) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}
}
