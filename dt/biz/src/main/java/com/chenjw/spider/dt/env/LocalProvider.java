package com.chenjw.spider.dt.env;

import java.io.IOException;
import java.util.Properties;

public class LocalProvider implements EnvProvider {
	private static Properties PROPERTIES;

	public Properties getProperties() {
		init();
		return PROPERTIES;
	}

	private static void init() {
		if (PROPERTIES != null) {
			return;
		}
		try {
			PROPERTIES = new Properties();
			PROPERTIES.load(LocalProvider.class.getClassLoader()
					.getResourceAsStream("env/local/db.properties"));
		} catch (IOException e) {
		}
	}

	public boolean isEnable() {
		return "chenjw".equals(System.getenv("USER"));

	}

	@Override
	public String getName() {
		return "local";
	}

	@Override
	public int getInstanceCount() {
		return 1;
	}

	@Override
	public int getInstanceIndex() {
		return 0;
	}
}
