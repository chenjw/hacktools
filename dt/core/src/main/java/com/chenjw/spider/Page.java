package com.chenjw.spider;

import java.util.Map;

import com.chenjw.client.exception.ErrorCodeEnum;

public class Page {

	private boolean isSuccess;

	private ErrorCodeEnum errorCode;

	private Map<String, String> context;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public ErrorCodeEnum getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodeEnum errorCode) {
		this.errorCode = errorCode;
	}

	public Map<String, String> getContext() {
		return context;
	}

	public void setContext(Map<String, String> context) {
		this.context = context;
	}

}
