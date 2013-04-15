package com.chenjw.spider.dt.web.app.utils;

import com.alibaba.citrus.extension.rpc.RPCRequestContext;
import com.alibaba.citrus.extension.rpc.RPCResultGenerator;
import com.alibaba.citrus.extension.rpc.validation.ErrorContext;

public class SimpleResultGenerator implements RPCResultGenerator {

	@Override
	public Object generate(Object result, RPCRequestContext requestContext,
			ErrorContext errorContext) {
		return result;
	}

}
