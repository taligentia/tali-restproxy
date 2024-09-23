package com.taligentia.sharepointrestproxy.model;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class QueryProxy {
	@NotNull
	private Map<String, String> request;
	private boolean debug;

	public QueryProxy(String dataset) {
	}

	public QueryProxy() {
		this(null);
	}

	public String asText() {
		return toString();
	}

	public Map<String, String> getRequest() {
		return request;
	}

	public void setRequest(Map<String, String> request) {
		this.request = request;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
