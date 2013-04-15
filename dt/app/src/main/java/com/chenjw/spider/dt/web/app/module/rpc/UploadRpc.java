package com.chenjw.spider.dt.web.app.module.rpc;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.model.WeiboException;

import com.alibaba.citrus.extension.rpc.annotation.ResourceMapping;

import com.alibaba.nonda.databind.annotation.RequestParam;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;


public class UploadRpc {

	@Autowired
	private WeiboService weiboService;
	
	private String getErrorMessage(Exception e){
		if(!(e instanceof WeiboException)){
			return "未知原因";
		}
		while(e.getCause() !=null && e!=e.getCause() && (e.getCause() instanceof WeiboException)){
			e=(WeiboException)e.getCause();
		}
		return ((WeiboException)e.getCause()).getError();
	}
	
	@ResourceMapping
	public Map<String, Object> upload(@RequestParam(name = "pic") String pic,
			@RequestParam(name = "status") String status, HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();

		pic = StringUtils.substringAfter(pic, ",");
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		try {
			
			weiboService.upload(userToken, status, Base64.decodeBase64(pic.getBytes()));
			result.put("success", true);
		} 
		catch (Exception e) {
			result.put("errorMessage", getErrorMessage(e));
			result.put("success", false);
		}
		return result;
	}
}
