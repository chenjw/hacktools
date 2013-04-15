package com.chenjw.spider.tbk.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.chenjw.client.exception.HttpClientException;
import com.chenjw.client.impl.SimpleHttpClient;
import com.chenjw.spider.tbk.LinkTransService;

public class LinkTransServiceImpl implements LinkTransService {
	private static final String cookie = "cna=KpgKCfWRO2cCAbedWyVsUAiV; lzstat_uv=3610104868133431042|700373@390770@359586@2876347; fresh_guide=%7B%22pid%22%3Atrue%7D; cookie1=508662c39f073f783955abfabed553e0; v=0; lzstat_ss=2606568104_6_1352465441_700373|1931626792_1_1352465421_390770|192203481_1_1352465421_359586|2580945494_0_1352465393_2876347; _tb_token_=786bab99a3b8; cookie27=VEI%3D; cookie3=Y2p3MTk4M0BnbWFpbC5jb20%3D; alimamapwag=TW96aWxsYS81LjAgKFgxMTsgVTsgTGludXggeDg2XzY0OyB6aC1DTjsgcnY6MS45LjIuMjYpIEdlY2tvLzIwMTIwMjAxIENlbnRPUy8zLjYuMjYtMS5lbDYuY2VudG9zIEZpcmVmb3gvMy42LjI2; cookie10=Y2hlbmp3X3Ri; login=VT5L2FSpMGV7TQ%3D%3D; cookie2=MzAwNDQ3MDQ%3D; alimamapw=UFtQXg5BPBVSMFJTUwNXB1IPA1EAB10EAAcAWlFRBAQDV1IJVQcEVAFU; tlut=UoLZXibIBJaHuA%3D%3D";
	private static final String url = "http://www.alimama.com/union/spread/activities/linkTransTools.do";

	private SimpleHttpClient httpClient = new SimpleHttpClient();
	{
		httpClient.init();
	}

	@Override
	public String trans(String itemUrl) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("promotionURL", itemUrl);
		params.put("type", "tmall");
		try {
			String result = httpClient.post(url, params, "UTF-8", cookie);
			Map<String, String> rm = JSON.parseObject(result, Map.class);
			return rm.get("msg");
		} catch (HttpClientException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {
		LinkTransServiceImpl t = new LinkTransServiceImpl();
		String u = t.trans("http://detail.tmall.com/item.htm?id=15752156081");
		System.out.println(u);
	}
}
