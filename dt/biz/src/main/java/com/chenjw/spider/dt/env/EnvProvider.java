package com.chenjw.spider.dt.env;

import java.util.Properties;

public interface EnvProvider {
	public int getInstanceCount();

	public int getInstanceIndex();

	public Properties getProperties();

	public boolean isEnable();

	public String getName();
}
