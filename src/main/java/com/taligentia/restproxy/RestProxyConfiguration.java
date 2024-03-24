package com.taligentia.restproxy;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taligentia.base.bearer.BearerConfiguration;
import com.taligentia.base.dropwizard.BaseConfiguration;

import com.taligentia.restproxy.proxy.ProxyConfiguration;
import io.dropwizard.client.JerseyClientConfiguration;

public class RestProxyConfiguration extends BaseConfiguration {
	@NotNull
	private RestProxyManagerConfiguration restProxy;

	@NotNull
	private ProxyConfiguration proxy;

	@Valid
	@NotNull
	private BearerConfiguration auth;

	@Valid
	@NotNull
	private JerseyClientConfiguration jerseyClientConfiguration;
	   
	@JsonProperty("restProxy")
	public RestProxyManagerConfiguration getRestProxy() {
		return restProxy;
	}

	@JsonProperty("proxy")
	public ProxyConfiguration getProxy() {
		return proxy;
	}

	@JsonProperty("auth")
	public BearerConfiguration getAuth() {
		return auth;
	}

	@JsonProperty("jerseyClient")
	public JerseyClientConfiguration getJerseyClientConfiguration() {
		return jerseyClientConfiguration;
	}

	public void setRestProxy(RestProxyManagerConfiguration restProxy) {
		this.restProxy = restProxy;
	}

	@Override
	public Map<String, Map<String, String>> getViewRendererConfiguration() {
		return Map.of();
	}

}
