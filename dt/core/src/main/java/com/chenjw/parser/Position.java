package com.chenjw.parser;

import org.htmlparser.Node;

import com.chenjw.parser.utils.TemplateDesc;

public class Position {
	private Node node;
	private TemplateDesc desc;

	public Position(Node node, TemplateDesc desc) {
		this.node = node;
		this.desc = desc;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public TemplateDesc getDesc() {
		return desc;
	}

	public void setDesc(TemplateDesc desc) {
		this.desc = desc;
	}

}
