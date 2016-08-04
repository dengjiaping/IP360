package com.truthso.ip360.net;

import java.io.Serializable;

 public class BaseHttpResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
	private String key;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
