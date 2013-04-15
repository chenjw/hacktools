package com.chenjw.client.exception;

public class HttpClientException extends Exception {

	private static final long serialVersionUID = 7409744286674919442L;
	private ErrorCodeEnum errorCode;

	public HttpClientException(ErrorCodeEnum errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public HttpClientException(ErrorCodeEnum errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public HttpClientException(ErrorCodeEnum errorCode, String message,
			Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public HttpClientException(ErrorCodeEnum errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCodeEnum getErrorCode() {
		return errorCode;
	}

}
