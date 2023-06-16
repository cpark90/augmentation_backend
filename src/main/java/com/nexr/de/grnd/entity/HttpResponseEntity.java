package com.nexr.de.grnd.entity;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpResponseEntity {
	private boolean error;
	private String responseMessage;
	private int statusCode;
	private String errorMessage;
	//클라이언트로 전송할 데이터
	private Map<String, Object> responseData;
	
	public Map<String, Object> getResponseData() {
		return responseData;
	}

	public void setResponseData(Map<String, Object> responseData) {
		this.responseData = responseData;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isError() {
		return error;
	}
	
	public void setError(boolean error) {
		this.error = error;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	@Override
	public String toString() {
		Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
		return getClass().getName() + "[error=" + error 
				+ ", statusCode=" + statusCode 
				+ ", errorMessage=" + errorMessage 
				+ ", responseMessage=" + responseMessage 
				+ ", responseData=" + gson.toJson(responseData) 
				+ "]";
	}
}
