package com.chenjw.spider.dt.web.app.valve;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.service.UserService;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

public class ForceLoginValve extends BaseValve {

	@Autowired
	private UserService userService;

	public void invoke(PipelineContext pipelineContext) throws Exception {
		TurbineRunDataInternal rundata = (TurbineRunDataInternal) getTurbineRunData(request);
		HttpSession session = rundata.getRequest().getSession();
		// 强制登录
		String force = rundata.getParameters().getString("force");
		if (!StringUtils.isBlank(force)) {
			TokenModel userToken = userService.findWatchedUserById(force);
			session.setAttribute(DtConstants.USER_SESSION_KEY, userToken);
		}
		pipelineContext.invokeNext();
	}

}