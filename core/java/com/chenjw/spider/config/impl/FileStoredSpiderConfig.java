package com.chenjw.spider.config.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.chenjw.spider.config.PageConfig;

public class FileStoredSpiderConfig extends SimpleSpiderConfig {
	private String folderPath;
	private File baseFolder;
	private Map<String, File> pageFolders = new HashMap<String, File>();
	private static final String FILE_URL = "url.dat";
	private static final String FILE_PARAMS = "params.dat";
	private static final String FILE_ENCODING = "encoding.dat";
	private static final String FOLDER_TRAIN_HTMLS = "train_htmls";

	private static final String DEFAULT_ENCODING = "utf-8";

	public void init() {
		baseFolder = new File(folderPath);
		for (File moduleFolder : baseFolder.listFiles()) {
			if (moduleFolder.isDirectory()) {
				for (File pageFolder : moduleFolder.listFiles()) {
					if (pageFolder.isDirectory()) {
						String pageId = moduleFolder.getName() + File.separator
								+ pageFolder.getName();
						pageFolders.put(pageId, pageFolder);
						PageConfig pageConfig = readPage(pageFolder);
						addPage(pageId, pageConfig);
					}
				}

			}
		}

	}

	private PageConfig readPage(File pageFolder) {
		String encoding = readFile(pageFolder, FILE_ENCODING, DEFAULT_ENCODING);
		SimplePageConfig pageConfig = new SimplePageConfig();
		pageConfig.setEncoding(encoding);
		String url = readFile(pageFolder, FILE_URL, encoding);
		pageConfig.setUrl(url);
		String paramStr = readFile(pageFolder, FILE_PARAMS, encoding);
		Map<String, String> paramMap = new HashMap<String, String>();
		if (!StringUtils.isBlank(paramStr)) {
			for (String pair : paramStr.split("&")) {
				String[] pairArray = pair.split("=");
				paramMap.put(pairArray[0], pairArray[1]);
			}
		}
		pageConfig.setParams(paramMap);
		String[] trainHtmls = readFolder(pageFolder, FOLDER_TRAIN_HTMLS,
				encoding);
		pageConfig.setTrainHtmls(trainHtmls);
		return pageConfig;
	}

	private String readFile(File parentFolder, String fileName, String encoding) {
		File f = new File(parentFolder, fileName);
		try {
			String result = FileUtils.readFileToString(f, encoding);
			if (result != null) {
				result = result.trim();
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String[] readFolder(File parentFolder, String folderName,
			String encoding) {
		List<String> result = new ArrayList<String>();
		File fd = new File(parentFolder, folderName);
		if (fd.exists() && fd.isDirectory()) {
			for (String fileName : fd.list()) {
				result.add(readFile(fd, fileName, encoding));
			}
		}
		return result.toArray(new String[result.size()]);
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

}
