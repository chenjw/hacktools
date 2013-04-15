package com.chenjw.spider.dt.service;

public interface DeletedTweetCheckService {

	public void check(String userId);

	public void checkAll();

	public void checkByName(String name);

	public void start();

	public void stop();
}
