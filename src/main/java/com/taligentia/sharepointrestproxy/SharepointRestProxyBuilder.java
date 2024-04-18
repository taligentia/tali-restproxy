package com.taligentia.sharepointrestproxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taligentia.base.bearer.BearerConfiguration;
import com.taligentia.base.dropwizard.BaseBuilder;
import com.taligentia.base.dropwizard.BaseBuilderImpl;
import com.taligentia.sharepointrestproxy.proxy.ProxyManager;

import io.dropwizard.lifecycle.setup.ExecutorServiceBuilder;
import io.dropwizard.setup.Environment;

public class SharepointRestProxyBuilder extends BaseBuilderImpl implements BaseBuilder {
	private SharepointRestProxyManagerConfiguration restProxyManagerConfiguration;
	private BearerConfiguration bearerConfiguration;
	private ExecutorService executorService;
	private ExecutorServiceBuilder executorServiceBuilder;
	private ProxyManager proxyManager;
	
	public SharepointRestProxyBuilder(Environment environment, String name) {
		super();
    	setExecutorServiceBuilder( environment
    			.lifecycle()
				.executorService(getName() + "-%d")
				.threadFactory(new ThreadFactoryBuilder().setDaemon(true).build())
				.rejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy())
		);
	}

	public SharepointRestProxyBuilder() {
    	super();
		setExecutorServiceBuilder(null);
	}

	private void setExecutorServiceBuilder( ExecutorServiceBuilder executorServiceBuilder ) {
        this.executorServiceBuilder = executorServiceBuilder;
        this.executorService = null;
	}
	
   public SharepointRestProxyBuilder using(ExecutorService executorService) {
    	this.executorService = executorService;
    	return this;
    }
    
    public SharepointRestProxyBuilder using(SharepointRestProxyManagerConfiguration configuration) {
        this.restProxyManagerConfiguration = configuration;
        if( executorServiceBuilder != null ) {
        	executorServiceBuilder
        		.minThreads(configuration.getPoolThreadSize())
        		.maxThreads(configuration.getPoolThreadSize());
        	this.using( executorServiceBuilder.build());
        }
        return this;
    }

    public SharepointRestProxyBuilder using(BearerConfiguration bearerConfiguration) {
        this.bearerConfiguration = bearerConfiguration;
        return this;
    }

	public SharepointRestProxyBuilder using(ProxyManager proxyManager) {
        this.proxyManager = proxyManager;
        return this;
	}

	@Override
	public SharepointRestProxyManager build(String string) {
		if( executorService == null )
			throw new IllegalArgumentException("ExecutorService is not defined ");
		final SharepointRestProxyManager manager = new SharepointRestProxyManager(
				proxyManager,
				bearerConfiguration,
				restProxyManagerConfiguration
		);
		manager.setName(getName());
		final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
		manager.setExecutorService( listeningExecutorService );
		addInfoManager(manager);
		return manager;
	}

}
