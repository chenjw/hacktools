package com.chenjw.parser;

import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;

import com.chenjw.parser.rule.ClassRule;
import com.chenjw.parser.rule.IdRule;
import com.chenjw.parser.rule.ParentRule;
import com.chenjw.parser.rule.SiblingRule;
import com.chenjw.parser.rule.TagRule;

public class RuleEngine {
	public List<Rule> ruleList = new ArrayList<Rule>();
	{

		ruleList.add(new ClassRule());
		ruleList.add(new IdRule());
		ruleList.add(new ParentRule());
		ruleList.add(new SiblingRule());
		ruleList.add(new TagRule());
	}

	public int score(Node baseNode, Node newNode) {
		if (baseNode == null || newNode == null) {
			return 0;
		}
		if (baseNode.getClass() != newNode.getClass()) {
			return 0;
		}
		int score = countScore(baseNode, newNode);
		score += 2 * score(baseNode.getParent(), newNode.getParent());
		return score;
	}

	private int countScore(Node baseNode, Node newNode) {
		int score = 0;
		for (Rule rule : ruleList) {
			score += rule.isLike(baseNode, newNode);
		}
		return score;
	}

}
