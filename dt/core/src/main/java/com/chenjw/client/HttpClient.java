package com.chenjw.client;

import com.chenjw.client.exception.HttpClientException;
import com.chenjw.spider.location.HttpUrl;

public interface HttpClient {
	public String get(HttpUrl url, String encoding, String cookie)
			throws HttpClientException;

	public byte[] getBytes(HttpUrl url, String cookie)
			throws HttpClientException;

	public String post(HttpUrl url, String encoding, String cookie)
			throws HttpClientException;
}
