package com.chenjw.parser.rule;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Tag;

import com.chenjw.parser.Rule;

public class ClassRule implements Rule {

	@Override
	public int isLike(Node baseNode, Node newNode) {
		if(getClass(baseNode)==null){
			if(getClass(newNode)==null){
				return 0;
			}
			else{
				return -1;
			}
		}
		else{
			if(getClass(newNode)==null){
				return -1;
			}
			else{
				if(StringUtils.equals(getClass(baseNode), getClass(newNode))){
					return 1;
				}
				else{
					return -1;
				}
			}
		}
	}
	
	
	private String getClass(Node node){
		if(node instanceof Tag){
			Tag tag=(Tag)node;
			return tag.getAttribute("class");
		}
		return null;
	}
}
