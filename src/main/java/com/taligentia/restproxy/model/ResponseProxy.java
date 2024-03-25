package com.taligentia.restproxy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResponseProxy {
	private QueryProxy query;
	private String response;

	public ResponseProxy() {
	}
	
	public QueryProxy getQuery() {
		return query;
	}

	public ResponseProxy setQuery(QueryProxy query) {
		this.query = query;
		return this;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
