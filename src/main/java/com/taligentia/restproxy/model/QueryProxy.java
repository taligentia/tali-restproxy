package com.taligentia.restproxy.model;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class QueryProxy {
	@NotNull
	private UserContext userContext;

	private String acceptHeader;
	private Map<String, Object> request;
	private boolean debug;

	public QueryProxy(String dataset) {
	}

	public QueryProxy() {
		this(null);
	}

	public UserContext getUserContext() {
		return userContext;
	}

	public QueryProxy setUserContext(UserContext userContext) {
		this.userContext = userContext;
		return this;
	}

	public String asText() {
		return toString();
	}
}
