package com.chenjw.parser.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Text;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

import com.chenjw.parser.HtmlParser;
import com.chenjw.parser.Position;
import com.chenjw.parser.RuleEngine;
import com.chenjw.parser.utils.TemplateDesc;
import com.chenjw.parser.utils.TemplateUtils;

public class SimpleHtmlParser implements HtmlParser {

	private final List<Position> experience = new ArrayList<Position>();
	private final RuleEngine ruleEngine = new RuleEngine();

	@Override
	public Map<String, String> read(String html) {
		final Map<String, NodeScore> bingoNodeScoreMap = new HashMap<String, NodeScore>();
		Parser parser = Parser.createParser(html, null);
		try {
			parser.visitAllNodesWith(new NodeVisitor() {

				@Override
				public void visitStringNode(Text text) {
					if (text.getText().replaceAll("\n", "").trim().length() == 0) {
						return;
					}
					for (Position pos : experience) {
						int score = countScore(pos, text);
						Map<String, String> result = TemplateUtils.parse(
								text.toPlainTextString(), pos.getDesc());
						for (Entry<String, String> entry : result.entrySet()) {
							if (entry.getValue() == null) {
								continue;
							}
							if (bingoNodeScoreMap.get(entry.getKey()) == null
									|| score > bingoNodeScoreMap.get(entry
											.getKey()).score) {
								bingoNodeScoreMap.put(entry.getKey(),
										new NodeScore(entry.getValue(), score));
							}
						}

					}
				}

			});
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, String> result = new HashMap<String, String>();
		for (Entry<String, NodeScore> entry : bingoNodeScoreMap.entrySet()) {
			result.put(entry.getKey(), StringUtils.trim(entry.getValue().text));
		}
		return result;

	}

	private class NodeScore {
		private String text;
		private int score;

		public NodeScore(String text, int score) {
			this.text = text;
			this.score = score;
		}
	}

	private int countScore(Position pos, Node newNode) {
		int score = ruleEngine.score(pos.getNode(), newNode);
		// System.out.println("[node]"
		// + pos.getNode().getText().replaceAll("\n", "").trim()
		// + "[score]" + score);

		return score;
	}

	@Override
	public void train(String template) {
		Parser parser = Parser.createParser(template, null);
		try {
			parser.visitAllNodesWith(new NodeVisitor() {

				@Override
				public void visitStringNode(Text text) {
					TemplateDesc desc = TemplateUtils.build(text
							.toPlainTextString());
					if (desc.isTemplate()) {
						experience.add(new Position(text, desc));
					}
				}

			});
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
