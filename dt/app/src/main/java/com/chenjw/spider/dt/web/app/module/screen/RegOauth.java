package com.chenjw.spider.dt.web.app.module.screen;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.util.WeiboConfig;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.alibaba.citrus.turbine.dataresolver.Param;
import com.chenjw.spider.dt.constants.UserStatusEnum;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

//3221292113
/**
 * 
 * @author chenjw
 * 
 */
public class RegOauth {
	@Autowired
	private UserService userService;
	@Autowired
	private WeiboService weiboService;

	public void execute(@Param(name = "code") String code, Context context,
			HttpSession session, Navigator navigator) {
		TokenModel register = (TokenModel) session
				.getAttribute(DtConstants.REGISTER_USER_SESSION_KEY);
		WeiboConfig.setClientInfo(register);
		// redirect_URI
		// client_ID
		if (StringUtils.isBlank(code)) {

			// context.put("register",register);
			String oauthUrl = weiboService.findAuthorizeUrl(register
					.getClientId());
			System.out.println(oauthUrl);
			navigator.redirectToLocation(oauthUrl);
		} else {
			String token = null;
			//weiboService.findAccessTokenByCode(code);
			
			register.setToken(token);
			TokenModel model = userService.findWatchedUserByToken(token);
			// 未开通权限
			if (model == null) {
				// 添加一个未开通权限的用户
				model = userService.addUser(token, register.getClientId(),
						register.getClientSecret());
			} else {
				boolean changed = false;
				if (!model.isValid()) {
					model.setStatus(UserStatusEnum.FOREVER_VALID);
					changed = true;
				}
				if (!StringUtils.equals(model.getToken(), token)) {
					model.setToken(token);
					changed = true;
				}
				if (changed) {
					userService.updateUser(model);
				}
			}
			// 已开通权限
			session.setAttribute(DtConstants.USER_SESSION_KEY, model);
			session.setAttribute(DtConstants.LOGIN_USER_SESSION_KEY, model);
			navigator.redirectToLocation("/");
		}

	}

}