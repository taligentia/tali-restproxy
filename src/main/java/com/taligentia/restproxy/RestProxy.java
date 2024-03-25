package com.taligentia.restproxy;


import java.util.LinkedList;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.taligentia.restproxy.proxy.ProxyManager;
import com.taligentia.restproxy.proxy.ProxyManagerBuilder;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;
import com.taligentia.base.bearer.BaseAuthenticator;
import com.taligentia.base.dropwizard.BaseApplication;
import com.taligentia.base.dropwizard.utils.PathsFilter;
import com.taligentia.restproxy.resources.ApiAuth;
import com.taligentia.restproxy.resources.ApiProxy;
import com.taligentia.restproxy.resources.ApiInfo;

import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class RestProxy extends Application<RestProxyConfiguration> {
	public static void main(String[] args) throws Exception {
		new RestProxy().run(args);
	}

	public RestProxy() {
		super();
	}
	
	public static String getVersion() {
		return BaseApplication.getVersion(RestProxy.class);
	}
	
	@Override
	public String getName() {
		return BaseApplication.getName(RestProxy.class);
	}
	
	@Override
	public void initialize(Bootstrap<RestProxyConfiguration> bootstrap) {
		BaseApplication.initialize(bootstrap);
	}
	
	@Override
	public void run(RestProxyConfiguration config, Environment environment) {

		final Client jerseyClient = new JerseyClientBuilder(environment)
				.using(config.getJerseyClientConfiguration())
				.build("jerseyClient");
		jerseyClient.register(MultiPartFeature.class);

		ProxyManager proxyManager = new ProxyManagerBuilder(environment)
				.using( config.getProxy())
				.build("proxy");

		final RestProxyBuilder mainBuilder = new RestProxyBuilder(environment,getName())
				.using(proxyManager)
				.using(config.getAuth())
				.using(config.getRestProxy());
		final RestProxyManager mainManager = mainBuilder.build(getName());

		environment.jersey().register(new ExceptionMapper<JsonMappingException> () {
			@Override
			public Response toResponse(JsonMappingException ex) {
				mainManager.getLogger().debug( ex.getMessage() );
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
		});
		
		BaseApplication.manage(environment, mainManager, mainBuilder);
		BaseApplication.registerhealthChecks(environment, mainBuilder);

		// Authentification de l'api
		final BaseAuthenticator baseAuthenticator = new RestProxyAuthentificator(mainManager);
		baseAuthenticator.register(environment.jersey());

		// Mise en place des Server Filter (PathsFilter implements ContainerRequestFilter) sue les API
		// https://www.baeldung.com/jersey-filters-interceptors
		LinkedList<String> uris = new LinkedList<String>();

		// API Auth
		uris.addAll(Lists.newArrayList(ApiAuth.Pages));
		environment.jersey().register(new ApiAuth(mainManager));

		// API Info
		uris.addAll(Lists.newArrayList(ApiInfo.Pages));
		environment.jersey().register(new ApiInfo(mainManager));

		// API Organisations
		uris.addAll(Lists.newArrayList(ApiProxy.Pages));
		environment.jersey().register(new ApiProxy(mainManager));

		// Swagger
	    BaseApplication.addSwager(environment,ApiAuth.class.getPackage().getName());
	    BaseApplication.allowCORSFilter(environment.getApplicationContext().getServletContext(), "*");
	    uris.add( "openapi.*" );

	    // Filtres d'accès aux resources
		String filtres = String.join("|", uris);
		mainManager.getLogger().info("filtres "+filtres );
		environment.jersey().register(new PathsFilter(filtres));
	}
}