package com.chenjw.spider.dt.web.app.valve;

import static com.alibaba.citrus.turbine.util.TurbineUtil.getTurbineRunData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.util.WeiboConfig;

import com.alibaba.citrus.service.pipeline.PipelineContext;
import com.alibaba.citrus.service.pipeline.support.AbstractValve;
import com.alibaba.citrus.turbine.TurbineRunDataInternal;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.spider.dt.web.app.constants.DtConstants;

/**
 * 用于设置微博api的调用参数
 * 
 * @author chenjw
 *
 */
public class WeiboConfigValve extends AbstractValve {

	@Autowired
	private HttpServletRequest request;

	@Override
	protected void init() {
		//
	}

	public void invoke(PipelineContext pipelineContext) throws Exception {
		TurbineRunDataInternal rundata = (TurbineRunDataInternal) getTurbineRunData(request);
		HttpSession session = rundata.getRequest().getSession();

		// 已登录
		TokenModel userToken = (TokenModel) session
				.getAttribute(DtConstants.USER_SESSION_KEY);
		if (userToken != null) {
			WeiboConfig.setClientInfo(userToken);
		}
		pipelineContext.invokeNext();
		return;
	}

}