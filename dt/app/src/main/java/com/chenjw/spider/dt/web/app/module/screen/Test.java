/*
 * Copyright 1999-2004 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.apache.xerces.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.client.HttpClient;
import com.chenjw.spider.dt.web.app.module.Base;
import com.chenjw.spider.location.HttpUrl;

public class Test extends Base {

	@Autowired
	private HttpClient httpClient;

	public void execute(@Param(name = "url") String url, Context context,
			Navigator navigator, HttpSession session) {
		url = "http://tp2.sinaimg.cn/2268916473/50/5608371350/1";
		byte[] bytes = null;
		boolean isSuccess = true;
		try {
			bytes = httpClient.getBytes(new HttpUrl(url, null), null);
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess = false;
		}
		context.put("success", isSuccess);

		String s = "data:image/png;base64,";
		context.put("data", s + Base64.encode(bytes));
	}
}
