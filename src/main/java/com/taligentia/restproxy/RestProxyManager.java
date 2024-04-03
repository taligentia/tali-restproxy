package com.taligentia.restproxy;

import com.taligentia.restproxy.model.*;
import com.taligentia.restproxy.proxy.ProxyConfiguration;
import com.taligentia.restproxy.proxy.ProxyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.health.HealthCheck.ResultBuilder;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.taligentia.base.bearer.BearerConfiguration;
import com.taligentia.base.bearer.BearerManager;
import com.taligentia.base.dropwizard.BaseManager;
import com.taligentia.base.dropwizard.BaseManagerImpl;
import com.taligentia.base.dropwizard.InfoManager;
import com.taligentia.base.dropwizard.ManagerInfo;


import io.dropwizard.lifecycle.Managed;

public class RestProxyManager extends BaseManagerImpl implements Managed,BaseManager,InfoManager  {
	final RestProxyManagerConfiguration configuration;
	final ProxyManager proxyManager;
	private String name;
	final BearerManager bearerManager;
	private ListeningExecutorService executorService;
	
	public RestProxyManager(
			ProxyManager proxyManager,
			BearerConfiguration bearerConfiguration,
			RestProxyManagerConfiguration configuration) {
		super();
		this.proxyManager = proxyManager;
		this.configuration = configuration;
		this.bearerManager = new BearerManager(bearerConfiguration);
		setName("");
		setLogger(LoggerFactory.getLogger(RestProxyManager.class));
	}	

	public void setLogger(Logger logger) {
		super.setLogger(logger);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return RestProxy.getVersion() == null ? "_" : RestProxy.getVersion();
	}
	
	private String getAllName() {
		return getName()+"-"+ getVersion();
	}
	
	public void setExecutorService(ListeningExecutorService listeningExecutorService) {
		this.executorService = listeningExecutorService;
	}

	public ListeningExecutorService getExecutorService() {
		return executorService;
	}

	public BearerManager getBearerManager() {
		return bearerManager;
	}

	public ProxyManager getProxyManager() {
		return proxyManager;
	}

	@Override
	public void start() throws Exception {
		setStartedTime(null);
		getLogger().info( "Initialisation de " + getAllName() + " " + getVersion() );
	}

	@Override
	public void stop() throws Exception {
		super.stop();
	}

	@Override
	public Result check() throws Exception {
		ResultBuilder builder = Result.builder();
		if( isStarted() ) {
			String version = getVersion();
			builder.healthy().withDetail("version", version);
		}
		else {
			builder.unhealthy();
		}
		return builder.build();
	}

	@SuppressWarnings("resource")
	public ManagerInfo getMangerInfo() {
		final RestProxyManager manager = this;
		return new ManagerInfo() {
			
			@Override
			public String getApplication() {
				return manager.getName();
			}

			@Override
			public String getVersion() {
				return manager.getVersion();
			}

			@Override
			public boolean isStarted() {
				return manager.isStarted();
			}
		};
	}

	public ResponseInfoVersion getInfoVersion() {
		ResponseInfoVersion responseInfoVersion = new ResponseInfoVersion(this);
		return responseInfoVersion;
	}

	public ResponseProxy process( QueryProxy queryProxy) {
		return proxyManager.process(queryProxy);
	}

	public RestProxyManagerConfiguration getConfiguration() {
		return configuration;
	}
}

