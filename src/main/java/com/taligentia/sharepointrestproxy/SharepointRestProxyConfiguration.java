package com.taligentia.sharepointrestproxy;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.taligentia.base.bearer.BearerConfiguration;
import com.taligentia.base.dropwizard.BaseConfiguration;

import com.taligentia.sharepointrestproxy.proxy.ProxyConfiguration;
import io.dropwizard.client.JerseyClientConfiguration;

public class SharepointRestProxyConfiguration extends BaseConfiguration {
	@NotNull
	private SharepointRestProxyManagerConfiguration restProxy;

	@NotNull
	private ProxyConfiguration proxy;

	@Valid
	@NotNull
	private BearerConfiguration auth;

	@Valid
	@NotNull
	private JerseyClientConfiguration jerseyClientConfiguration;

	@JsonProperty("restProxy")
	public SharepointRestProxyManagerConfiguration getRestProxy() {
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

	public void setRestProxy(SharepointRestProxyManagerConfiguration restProxy) {
		this.restProxy = restProxy;
	}

	@Override
	public Map<String, Map<String, String>> getViewRendererConfiguration() {
		return Map.of();
	}
}
