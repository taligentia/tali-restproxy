package com.taligentia.restproxy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.taligentia.base.bearer.BearerConfiguration;
import com.taligentia.base.dropwizard.BaseBuilder;
import com.taligentia.base.dropwizard.BaseBuilderImpl;
import com.taligentia.restproxy.proxy.ProxyManager;

import io.dropwizard.lifecycle.setup.ExecutorServiceBuilder;
import io.dropwizard.setup.Environment;

public class RestProxyBuilder extends BaseBuilderImpl implements BaseBuilder {
	private RestProxyManagerConfiguration hdtmEngineManagerConfiguration;
	private BearerConfiguration bearerConfiguration;
	
	private ExecutorService executorService;
	private ExecutorServiceBuilder executorServiceBuilder;
	private ProxyManager talidataProxyManager;
	
	public RestProxyBuilder(Environment environment, String name) {
		super();
    	setExecutorServiceBuilder( environment
    			.lifecycle()
				.executorService(getName() + "-%d")
				.threadFactory(new ThreadFactoryBuilder().setDaemon(true).build())
				.rejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy())
		);
	}

	public RestProxyBuilder() {
    	super();
		setExecutorServiceBuilder(null);
	}

	private void setExecutorServiceBuilder( ExecutorServiceBuilder executorServiceBuilder ) {
        this.executorServiceBuilder = executorServiceBuilder;
        this.executorService = null;
	}
	
   public RestProxyBuilder using(ExecutorService executorService) {
    	this.executorService = executorService;
    	return this;
    }
    
    public RestProxyBuilder using(RestProxyManagerConfiguration configuration) {
        this.hdtmEngineManagerConfiguration = configuration;
        if( executorServiceBuilder != null ) {
        	executorServiceBuilder
        		.minThreads(configuration.getPoolThreadSize())
        		.maxThreads(configuration.getPoolThreadSize());
        	this.using( executorServiceBuilder.build());
        }
        return this;
    }

    public RestProxyBuilder using(BearerConfiguration bearerConfiguration) {
        this.bearerConfiguration = bearerConfiguration;
        return this;
    }

	public RestProxyBuilder using(ProxyManager talidataProxyManager) {
        this.talidataProxyManager = talidataProxyManager;
        return this;
	}

	@Override
	public RestProxyManager build(String string) {
		if( executorService == null )
			throw new IllegalArgumentException("ExecutorService is not defined ");
		final RestProxyManager manager = new RestProxyManager(
				talidataProxyManager,
			bearerConfiguration,
			hdtmEngineManagerConfiguration		
		);
		manager.setName(getName());
		final ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);
		manager.setExecutorService( listeningExecutorService );
		addInfoManager(manager);
		return manager;
	}

}
