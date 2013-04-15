package com.chenjw.client;

import java.util.Map;

import com.chenjw.client.exception.HttpClientException;

public interface HttpClient {
	public String get(String url, Map<String, String> params, String encoding,
			String cookie) throws HttpClientException;

	public String post(String url, Map<String, String> params, String encoding,
			String cookie) throws HttpClientException;
}
