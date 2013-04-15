package com.chenjw.spider.dt.mapper;

import com.chenjw.spider.dt.dataobject.WatchedUserDO;
import com.chenjw.spider.dt.model.TokenModel;
import com.chenjw.tools.BeanCopyUtils;

public class TokenMapper {
	public static void model2Do(TokenModel model, WatchedUserDO dataobject) {
		BeanCopyUtils.copyProperties(dataobject, model);
	}

	public static void do2Model(WatchedUserDO dataobject, TokenModel model) {
		BeanCopyUtils.copyProperties(model, dataobject);
	}

}
