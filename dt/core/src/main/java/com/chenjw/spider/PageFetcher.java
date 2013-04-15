package com.chenjw.spider;

import com.chenjw.spider.location.HttpLocation;

public interface PageFetcher {

	public Page fetch(HttpLocation location);
}
