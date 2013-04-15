package com.chenjw.parser.rule;

import org.htmlparser.Node;

import com.chenjw.parser.Rule;

public class ParentRule implements Rule {

	@Override
	public int isLike(Node baseNode, Node newNode) {
		if(baseNode.getParent()==null && newNode.getParent()!=null){
			return -1;
		}
		if(baseNode.getParent()!=null && newNode.getParent()==null){
			return -1;
		}
		return 0;
	}
}
