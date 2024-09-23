package com.taligentia.sharepointrestproxy.model;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class QueryProxy {
	//@NotNull
	//private String acceptHeader;
	@NotNull
	private Map<String, String> request;
	private boolean debug;
	private String saveAs;
	//private String responseContentType;

	public QueryProxy(String dataset) {
	}

	public QueryProxy() {
		this(null);
	}

	public String asText() {
		return toString();
	}

	//public String getAcceptHeader() {
	//	return acceptHeader;
	//}

	//public void setAcceptHeader(String acceptHeader) {
	//	this.acceptHeader = acceptHeader;
	//}

	public Map<String, String> getRequest() {
		return request;
	}

	public void setRequest(Map<String, String> request) {
		this.request = request;
	}

	//public String getResponseContentType() {
	//	return responseContentType;
	//}

	//public void setResponseContentType(String responseContentType) {
	//	this.responseContentType = responseContentType;
	//}

	public String getSaveAs() {
		return saveAs;
	}

	public void setSaveAs(String saveAs) {
		this.saveAs = saveAs;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
