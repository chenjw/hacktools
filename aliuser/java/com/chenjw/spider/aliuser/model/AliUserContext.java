package com.chenjw.spider.aliuser.model;

public class AliUserContext {

	private AliUserInfo userInfo = new AliUserInfo();
	private boolean isFinished = false;

	public AliUserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(AliUserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

}
