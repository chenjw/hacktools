package com.chenjw.parser;

import org.htmlparser.Node;

public interface Rule {
	public int isLike(Node baseNode,Node newNode);
}
