package com.example.cm.cm_web.exceptions;

public class Error {
	private String code;
	private String message;

	public Error(String code, String message){
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
