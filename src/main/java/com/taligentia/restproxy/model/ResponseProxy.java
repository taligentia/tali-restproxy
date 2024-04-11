package com.taligentia.restproxy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(Include.NON_NULL)
public class ResponseProxy {
	private int httpStatusCode;

	private String httpStatusMessage;

	private String response;
	private JsonNode jsonResponse;

	public ResponseProxy() {
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public JsonNode getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(JsonNode jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getHttpStatusMessage() {
		return httpStatusMessage;
	}

	public void setHttpStatusMessage(String httpStatusMessage) {
		this.httpStatusMessage = httpStatusMessage;
	}
}
