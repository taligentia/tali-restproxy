package com.taligentia.sharepointrestproxy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.InputStream;

@JsonInclude(Include.NON_NULL)
public class ResponseProxy {
	private int httpStatusCode;
	private String httpStatusMessage;
	private String response;
	private JsonNode jsonResponse;
	private String contentType;
	private InputStream is;

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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}
}
