package com.chenjw.pom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ComparePom {
	public static List<String> check(File f) throws ParserConfigurationException,
			XPathExpressionException, SAXException, IOException {
		XPath path = XPathFactory.newInstance().newXPath();
		DocumentBuilder dbd = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		// 加载menu.xml文件到内存中
		Document doc = dbd.parse(f);
		NodeList list = (NodeList) path.evaluate(
				"//dependencyManagement/dependencies/dependency", doc,
				XPathConstants.NODESET);
		List<String> ss=new ArrayList<String>();
		for (int i = 0; i < list.getLength(); i++) {
			Node n = list.item(i);
			String groupId = (String) path.evaluate("groupId", n,
					XPathConstants.STRING);
			String artifactId = (String) path.evaluate("artifactId", n,
					XPathConstants.STRING);
			String key=groupId + " " + artifactId;
			if(ss.contains(key)){
				System.out.println(key);
			}
			else{
				ss.add(key);
			}
		
		}
		return ss;
	}
	
	public static void showAddition(List<String> left,List<String> right){
		for(String l:left){
			if(!right.contains(l)){
				System.out.println(l);
			}
		}
	}

	public static void main(String[] args) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		List<String> left=check(new File(
				"/home/chenjw/workspace/tfservice/tfservice/pom.xml"));
//		List<String> right=check(new File(
//				"/home/chenjw/workspace/tfservice/tfservice/pom.xml.merge-right.r10859"));
//		
//		System.out.println("left add");
//		showAddition(left,right);
//		System.out.println("right add");
//		showAddition(right,left);
	}
	
	
}
