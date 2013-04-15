package com.chenjw.parser;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.chenjw.parser.impl.SimpleHtmlParser;

public class ParserTest {
	public static SimpleHtmlParser parser = new SimpleHtmlParser();

	public static void train(String fileName) throws IOException {
		File file = new File(fileName);
		String html = FileUtils.readFileToString(file, "utf-8");
		parser.train(html);
		System.out.println("train " + fileName + " end!");
	}

	public static void read(String fileName) throws IOException {
		File file = new File(fileName);
		String html = FileUtils.readFileToString(file, "utf-8");

		Map<String, String> result = parser.read(html);
		for (Entry<String, String> entry : result.entrySet()) {
			System.out.println("find " + entry.getKey() + " = "
					+ entry.getValue());
		}
		System.out.println("read " + fileName + " end!");
	}

	public static void main(String[] args) throws IOException {
		ParserTest.train("/home/chenjw/test.html");

		ParserTest.train("/home/chenjw/test1.html");
		ParserTest.read("/home/chenjw/test2.html");
		System.out.println("finished!");
	}
}
