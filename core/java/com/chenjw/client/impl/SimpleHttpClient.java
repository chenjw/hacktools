package com.chenjw.client.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.params.HttpParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.chenjw.client.HttpClient;
import com.chenjw.client.exception.ErrorCodeEnum;
import com.chenjw.client.exception.HttpClientException;

public class SimpleHttpClient implements HttpClient {
	private int connectionTimeout = 3000;
	private int timeoutInMilliseconds = 5000;
	private int httpConnectionTimeout = 100000;
	private int socketTimeout = 100000;

	private int maxTotalConnections = 100;
	private MultiThreadedHttpConnectionManager connManager = null;
	private HttpMethodRetryHandler httpRequestRetryHandler = new DefaultHttpMethodRetryHandler(
			3, true);

	private HttpParams httpParams;

	public void init() {
		HttpConnectionParams httpParams = new HttpConnectionParams();
		httpParams.setConnectionTimeout(httpConnectionTimeout);
		/* 连接超时 */
		httpParams.setSoTimeout(socketTimeout);

		Protocol myhttps = new Protocol("https", new MySSLSocketFactory(), 443);
		Protocol.registerProtocol("https", myhttps);
		connManager = new MultiThreadedHttpConnectionManager();
		connManager.setMaxTotalConnections(maxTotalConnections);
		connManager.setMaxConnectionsPerHost(40);

	}

	private String prepareUrl(String url, Map<String, String> params,
			String encoding) {
		boolean isUrlHasParam = url.indexOf("?") != -1;
		StringBuffer sb = new StringBuffer(url);
		if (params != null) {
			boolean isFirst = true;
			for (Entry<String, String> entry : params.entrySet()) {
				if (!StringUtils.isBlank(entry.getValue())) {
					if (!isUrlHasParam && isFirst) {
						sb.append('?');
						isFirst = false;
					} else {
						sb.append('&');
					}
					try {
						sb.append(entry.getKey())
								.append('=')
								.append(URLEncoder.encode(entry.getValue(),
										encoding));
					} catch (UnsupportedEncodingException e) {
						//
					}
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String get(String url, Map<String, String> params, String encoding,
			String cookie) throws HttpClientException {
		String realUrl = prepareUrl(url, params, encoding);
		// System.out.println("url=" + realUrl);
		GetMethod method = new GetMethod(realUrl);
		if (cookie != null) {
			method.addRequestHeader("Cookie", cookie);
		}
		String result = excuteMethod(method, encoding);
		// System.out.println(result);
		return result;
	}

	@Override
	public String post(String url, Map<String, String> params, String encoding,
			String cookie) throws HttpClientException {
		String realUrl = url;
		// System.out.println("url=" + realUrl);
		PostMethod method = new PostMethod(realUrl);
		for (Entry<String, String> entry : params.entrySet()) {
			method.addParameter(new NameValuePair(entry.getKey(), entry
					.getValue()));
		}
		// String cookie =
		// "240=240; CurLoginUserGUID=a44e6f49751b4d8b9659636ae20540d6; strCertificate=410df02a24354be8bb194aeec0829589;";
		if (cookie != null) {
			method.addRequestHeader("Cookie", cookie);
		}
		String result = excuteMethod(method, encoding);
		// System.out.println(result);
		return result;
	}

	public String excuteMethod(HttpMethod method, String encoding)
			throws HttpClientException {
		int responseCode = -1;
		try {
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					httpRequestRetryHandler);
			org.apache.commons.httpclient.HttpClient client = this
					.createHttpClient();
			client.executeMethod(method);
			responseCode = method.getStatusCode();
			if (responseCode != HttpStatus.SC_OK) {
				throw new HttpClientException(ErrorCodeEnum.NOT_FOUND_ERROR,
						"responseCode:" + responseCode);
			} else {
				InputStream is = method.getResponseBodyAsStream();
				try {
					return IOUtils.toString(is, encoding);
				} finally {
					IOUtils.closeQuietly(is);
				}
			}
		} catch (HttpException e) {
			throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
					"HttpException", e);
		} catch (IOException e) {
			throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
					"HttpException", e);
		} finally {
			method.releaseConnection();
		}

	}

	/**
	 * 处理http请求的返回结果
	 * 
	 * @param responseCode
	 * @param method
	 * @return
	 * @throws IOException
	 */
	// private String handleResult(HttpResponse response,
	// HttpUriRequest httpMethod, String encoding)
	// throws HttpClientException {
	// int responseCode = response.getStatusLine().getStatusCode();
	// if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
	//
	// HttpEntity entity = response.getEntity();
	//
	// if (entity != null) {
	// String result = null;
	// try {
	// result = EntityUtils.toString(entity, encoding);
	// } catch (ParseException e) {
	// throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
	// "ParseException", e);
	// } catch (IOException e) {
	// throw new HttpClientException(ErrorCodeEnum.SYSTEM_ERROR,
	// "IOException", e);
	// }
	// return result;
	// }
	// }
	// throw new HttpClientException(ErrorCodeEnum.NOT_FOUND_ERROR,
	// "responseCode : " + responseCode);
	//
	// }

	private org.apache.commons.httpclient.HttpClient createHttpClient() {
		HttpClientParams clientParams = new HttpClientParams();
		org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient(
				clientParams, connManager);
		return httpClient;
	}

	public static void main(String[] args) throws HttpClientException {
		SimpleHttpClient c = new SimpleHttpClient();
		c.init();
		String cookie = "240=240; CurLoginUserGUID=a44e6f49751b4d8b9659636ae20540d6; strCertificate=410df02a24354be8bb194aeec0829589;";
		// String s = c
		// .get("http://res.gsjy.net/PLWeb/protect.aspx?AuthorizedInfo=%3cUserInfo%3e%3cCertificate%3e410df02a24354be8bb194aeec0829589%3c%2fCertificate%3e%3cUserId%3eFaa3rxGTExepUXV%2b2x3qLA%3d%3d%3c%2fUserId%3e%3cExpiredDate%3eqgTJ2eNxOozYDJJ6WlK7pee%2fNtTHmkegIfDookIcqQM%3d%3c%2fExpiredDate%3e%3cAuthorizedDate%3e2%2bkqOdYv2461V2Y6IEgpLICxMaD%2bv766ByLgMZGJiwY%3d%3c%2fAuthorizedDate%3e%3c%2fUserInfo%3e",
		// new HashMap(), "UTF-8");
		// System.out.println(s);
		for (int i = 0; i < 111; i++) {
			// c.get("http://res.gsjy.net/PLWeb/GroupManage.aspx?GroupInfoID=240",
			// new HashMap(), "UTF-8");
			c.get("http://res.gsjy.net/PLWeb/Common/DownAttach.aspx?ResID=68004ee21f4c489d9c0c14da482d4ed4&flag=Yes&GID=240",
					new HashMap(), "UTF-8", cookie);

		}

		// System.out.println(s);
	}
}
