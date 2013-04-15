package com.chenjw.spider.config.impl;

import java.net.URL;

public class ClasspathStoredSpiderConfig extends FileStoredSpiderConfig {
	private String classpath;

	public void init() {
		URL url = ClasspathStoredSpiderConfig.class.getClassLoader().getResource(
				classpath);
		this.setFolderPath(url.getFile());
		super.init();

	}

	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}

}
