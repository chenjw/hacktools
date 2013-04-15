package com.chenjw.spider.dt.web.app.valve;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.turbine.TurbineConstant;
import com.alibaba.citrus.turbine.TurbineRunData;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.alibaba.citrus.turbine.util.TurbineUtil;
import com.alibaba.citrus.util.StringUtil;
import com.alibaba.citrus.webx.WebxComponent;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.PermissionService;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public abstract class BaseValve extends AbstractValve {

	public static final String DELIM = ".";

	@Autowired
	protected WebxComponent component;
	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected PermissionService permissionService;
	@Autowired
	protected UserService userService;

	protected boolean isUserAvailable() {
		TurbineRunDataInternal rundata = (TurbineRunDataInternal) getTurbineRunData(request);
		HttpSession session = rundata.getRequest().getSession();

		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		if (userToken != null) {
			TokenModel dbToken = userService.findWatchedUserById(userToken
					.getUserId());
			if (dbToken != null && dbToken.isValid()) {
				return true;
			}
			// 已取消授权
			else {
				session.removeAttribute(DtConstants.USER_SESSION_KEY);
				session.removeAttribute(DtConstants.LOGIN_USER_SESSION_KEY);
				return false;
			}
		}
		return false;
	}

	protected boolean hasPermission() {
		// 如果不需要登录
		String urlEscaped = getUrlEscaped();
		return permissionService.havePermission(urlEscaped);
	}

	private String getUrlEscaped() {
		TurbineRunData rundata = TurbineUtil.getTurbineRunData(request);
		return getPermissionURI(rundata);

	}

	private String getPermissionURI(TurbineRunData rundata) {
		String uri = getActionPermissionURI(rundata);
		if (uri == null) {
			return getScreenPermissionURI(rundata);
		}
		else{
			return uri;
		}
	}

	private String getActionPermissionURI(TurbineRunData rundata) {
		String target = rundata.getParameters().getString("action");

		if (StringUtils.isBlank(target)) {
			return null;
		}
		target = getContent(target);
		return component.getName() + DELIM + TurbineConstant.ACTION_MODULE
				+ DELIM + target;
	}

	private String getScreenPermissionURI(TurbineRunData rundata) {
		String target = rundata.getTarget();

		target = getContent(target);
		if (target == null) {
			return null;
		}
		return component.getName() + DELIM + TurbineConstant.SCREEN_MODULE
				+ DELIM + target;
	}

	private String getContent(String target) {
		if (StringUtils.isEmpty(target)) {
			return null;
		}
		int dashIndex = target.lastIndexOf(DELIM);
		if (dashIndex != -1) {
			target = target.substring(0, dashIndex);
		}
		target = StringUtils.replace(target, "/", DELIM);
		if (target != null && target.startsWith(DELIM)) {
			target = StringUtils.substring(target, 1);
		}
		return StringUtil.toCamelCase(target);
	}

}