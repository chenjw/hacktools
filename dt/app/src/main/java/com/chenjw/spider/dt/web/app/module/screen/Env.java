package com.chenjw.spider.dt.web.app.module.screen;

import java.util.Map;

import com.alibaba.citrus.turbine.Context;
import com.alibaba.citrus.turbine.Navigator;
import com.chenjw.spider.dt.constants.EnvConstants;

public class Env {

	public void execute(Context context, Navigator navigator) {
		Map<Object, Object> map = EnvConstants.getEnvProvider().getProperties();
		context.put("env", map);
		context.put("sysEnv", System.getenv());
	}

}
