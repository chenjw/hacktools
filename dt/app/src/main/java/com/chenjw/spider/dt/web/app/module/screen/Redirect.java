package com.chenjw.spider.dt.web.app.module.screen;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.requestcontext.parser.ParameterParser;
import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.chenjw.spider.location.HttpUrl;
import com.chenjw.spider.location.UrlParseUtils;

//3221292113
/**
 * 
 * @author chenjw
 * 
 */
public class Redirect {
	@Autowired
	protected HttpServletRequest request;
	public void execute(Context context,
			Navigator navigator) {
		TurbineRunDataInternal runData = (TurbineRunDataInternal) TurbineUtil
				.getTurbineRunData(request);
		ParameterParser param = runData.getParameters();
		String url=param.getString("url");
		HttpUrl httpUrl=UrlParseUtils.parseUrl(url);
		for (String key : param.keySet()) {
			if (!"url".equals(key)) {
				httpUrl.getQueryParam().put(key, param.getString(key));
			}
		}
		url=httpUrl.toUrlString();
		runData.redirectToLocation(url);
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLEncoder.encode(
				"http://127.0.0.1:8080/regOauth.htm", "UTF-8"));
		// http://deleted-tweets.cloudfoundry.com/redirect.htm?url=http%3A%2F%2F127.0.0.1%3A8080%2FregOauth.htm
	}
}