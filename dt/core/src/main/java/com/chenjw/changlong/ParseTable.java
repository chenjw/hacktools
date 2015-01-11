package com.chenjw.changlong;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import au.com.bytecode.opencsv.CSVWriter;

public class ParseTable {

	public static void main(String[] args) throws IOException {
		Hacker h = new Hacker("http://info.loongair.cn/Dynamic.aspx?dd=杭州",
				"dd", HackTypeEnum.SEARCH, 28, 6);
		h.printXieRu();
		//h.printAllTablevalues("GJFOC", "T3017", "", "");

	}
}
