package com.taligentia.restproxy.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.taligentia.talidata.search.model.SearchResponse;

@JsonInclude(Include.NON_NULL)
public class ResponseProxy {
	private QueryProxy query;

	public ResponseProxy() {
	}
	
	public QueryProxy getQuery() {
		return query;
	}

	public ResponseProxy setQuery(QueryProxy query) {
		this.query = query;
		return this;
	}

	public ResponseProxy fromSearchResponse(SearchResponse searchResponse, String dataset ) {
		return this;
	}	

}
