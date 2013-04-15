package com.chenjw.spider.dt.web.app.valve;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.chenjw.spider.dt.constants.UserStatusEnum;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.WeiboService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class OAuthValve extends BaseValve {

	public static final String DELIM = ".";

	@Autowired
	private WeiboService weiboService;

	@Override
	protected void init() {
		//
	}

	public void invoke(PipelineContext pipelineContext) throws Exception {
		TurbineRunDataInternal rundata = (TurbineRunDataInternal) getTurbineRunData(request);
		HttpSession session = request.getSession();
		// 不需要登录就能访问的页面
		if (hasPermission()) {
			pipelineContext.invokeNext();
			return;
		}

		// 如果当前已登录，直接進入
		if (isUserAvailable()) {
			pipelineContext.invokeNext();
			return;
		}

		// rundata.forwardTo("welcome.vm");

		String token = parseTokenFromRequest(rundata, pipelineContext);
		// 如果请求中没有登录信息，跳转到登录页面
		if (token == null) {
			login(token, rundata, pipelineContext);
			return;
		}
		// 查询会员信息
		TokenModel model = userService.findWatchedUserByToken(token);
		// 未开通权限
		if (model == null) {
			TokenModel registerToken = (TokenModel) session
					.getAttribute(DtConstants.REGISTER_USER_SESSION_KEY);
			// 添加用户
			model=userService.addUser(token, registerToken.getClientId(),
					registerToken.getClientSecret());
		}
		// 已开通
		else {
			// 更新信息
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
		rundata.setRedirectLocation("/");
	}

	/**
	 * 从请求中获得token信息
	 * 
	 * @param rundata
	 * @param pipelineContext
	 * @return
	 */
	private String parseTokenFromRequest(TurbineRunDataInternal rundata,
			PipelineContext pipelineContext) {

		String code = rundata.getParameters().getString("code");
		if (StringUtils.isBlank(code)) {

			return null;
		}
		TokenModel registerToken = (TokenModel) request.getSession()
				.getAttribute(DtConstants.REGISTER_USER_SESSION_KEY);

		if (registerToken == null) {
			return null;
		}
		String token = weiboService.findAccessTokenByCode(code,
				registerToken.getClientId(), registerToken.getClientSecret());
		if (StringUtils.isBlank(token)) {
			return null;
		}
		return token;
	}

	private void oauth(String clientId, TurbineRunDataInternal rundata,
			PipelineContext pipelineContext) {
		// rundata.setTarget("oauth.vm");
		// pipelineContext.invokeNext();
		// 重定向到登录页面
		String redirectUrl = weiboService.findAuthorizeUrl(clientId);
		rundata.setRedirectLocation(redirectUrl);

	}

	private void login(String token, TurbineRunDataInternal rundata,
			PipelineContext pipelineContext) {
		pipelineContext.setAttribute("token", token);
		rundata.setTarget("login.vm");
		pipelineContext.invokeNext();
	}

	private void errorPage(TurbineRunDataInternal rundata,
			PipelineContext pipelineContext, String errorMessage) {
		rundata.getContext().put("errorMessage", errorMessage);
		rundata.setTarget("error.vm");
		pipelineContext.invokeNext();

	}

}