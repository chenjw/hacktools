package com.chenjw.spider.dt.env;

import java.io.IOException;
import java.util.Properties;

public class SaeProvider implements EnvProvider {
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
					.getResourceAsStream("env/sae/db.properties"));
		} catch (IOException e) {
		}
	}

	public boolean isEnable() {
		return true;

	}

	@Override
	public String getName() {
		return "sae";
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
