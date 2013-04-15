package com.chenjw.parser.rule;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;

import com.chenjw.parser.Rule;

public class SiblingRule implements Rule {

	@Override
	public int isLike(Node baseNode, Node newNode) {
		if (baseNode.getParent() == null || newNode.getParent() == null) {
			return 0;
		}
		if (getIndex(baseNode) == getIndex(newNode)) {
			return 1;
		} else {
			return -1;
		}
	}

	private int getIndex(Node node) {
		Node n = node;
		int index = 0;
		// String str = "";
		while (n.getPreviousSibling() != null) {
			n = n.getPreviousSibling();
			if (n instanceof TagNode) {
				index += 1;
				// str += ((TagNode) n).getTagName() + " ";
			}
		}
		// System.out.println("[sibling]" + str + "[index]" + index);
		return index;
	}

}
