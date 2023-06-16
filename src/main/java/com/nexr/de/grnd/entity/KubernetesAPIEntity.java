package com.nexr.de.grnd.entity;

import java.util.List;
import java.util.Map;

public class KubernetesAPIEntity {
	private int code;
	private String responseBody;
	private Map<String, List<String>> responseHeaders;
	private String localizedMessage;
	private String message;
	
	
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getResponseBody() {
		return responseBody;
	}
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}
	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}
	public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	public String getLocalizedMessage() {
		return localizedMessage;
	}
	public void setLocalizedMessage(String localizedMessage) {
		this.localizedMessage = localizedMessage;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
