package com.chenjw.spider.dt.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.chenjw.spider.dt.model.URLAccessObject;
import com.chenjw.spider.dt.service.PermissionService;

public class PermissionServiceImpl implements PermissionService,
		InitializingBean {

	private boolean denyAccess = false;

	private URLAccessObject urlProtected;

	private URLAccessObject urlNotProtected;

	public boolean havePermission(String urlEscaped) {
		Assert.hasText(urlEscaped, "accessName can not be empty");
		if (denyAccess) {
			if (urlNotProtected != null && urlNotProtected.contains(urlEscaped)) {
				return true;
			} else {
				return false;
			}
		} else {
			if (urlProtected != null && urlProtected.contains(urlEscaped)) {
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(urlProtected == null || urlNotProtected == null,
				"urlProtected and urlNotProtected can not both exist");
	}

	public void setDenyAccess(boolean denyAccess) {
		this.denyAccess = denyAccess;
	}

	private URLAccessObject parse(String str) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		URLAccessObject uao = new URLAccessObject();
		for (String s : str.split("\n")) {
			s = s.trim();
			if (!StringUtils.isBlank(s)) {
				uao.addUrlType(s);
			}
		}
		return uao;

	}

	public void setUrlProtected(String urlProtected) {
		this.urlProtected = parse(urlProtected);
	}

	public void setUrlNotProtected(String urlNotProtected) {
		this.urlNotProtected = parse(urlNotProtected);
	}

}
