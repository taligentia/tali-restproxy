package com.taligentia.restproxy.model;

import javax.validation.constraints.NotNull;

public class QueryProxy {
	@NotNull
	private UserContext userContext;

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
