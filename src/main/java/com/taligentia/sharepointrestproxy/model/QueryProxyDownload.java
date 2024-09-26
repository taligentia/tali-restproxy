package com.taligentia.sharepointrestproxy.model;

import javax.validation.constraints.NotNull;

public class QueryProxyDownload {
	@NotNull
	private String url;

	public QueryProxyDownload() {
	}

	public @NotNull String getUrl() {
		return url;
	}

	public void setUrl(@NotNull String url) {
		this.url = url;
	}
}
