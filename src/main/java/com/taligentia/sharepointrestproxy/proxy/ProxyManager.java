package com.taligentia.sharepointrestproxy.proxy;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taligentia.base.dropwizard.BaseManager;
import com.taligentia.base.dropwizard.InfoManager;
import com.taligentia.sharepointrestproxy.model.QueryProxy;
import com.taligentia.sharepointrestproxy.model.ResponseProxy;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;

public class ProxyManager implements Managed, BaseManager, InfoManager {
    public static final Logger logger = LoggerFactory.getLogger(ProxyManager.class);
    private ProxyConfiguration proxyConfiguration;

    public ProxyManager(ProxyConfiguration proxyConfiguration) throws IllegalArgumentException, ClassNotFoundException, IOException {
        this.proxyConfiguration = proxyConfiguration;
    }

    public ProxyConfiguration getProxyConfiguration() {
        return proxyConfiguration;
    }

    public void setProxyConfiguration(ProxyConfiguration proxyConfiguration) {
        this.proxyConfiguration = proxyConfiguration;
    }

    @Override
    public void setStopListener(StopListener stopListener) {
    }

    @Override
    public HealthCheck.Result check() throws Exception {
        return null;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
    }

    public ResponseProxy process(QueryProxy queryProxy) {
        ProxyHttpClient httpClient = new ProxyHttpClient();
        httpClient.setAcceptHeader(queryProxy.getAcceptHeader());
        getLogger().debug("SharepointRestProxy : " + queryProxy.getRequest().get("url"));
        httpClient.doGet(proxyConfiguration.getAuth("sharepointrestproxy").getMethod(),proxyConfiguration.getAuth("sharepointrestproxy").getUser(), proxyConfiguration.getAuth("sharepointrestproxy").getPasswd(), proxyConfiguration.getAuth("sharepointrestproxy").getDomain(), queryProxy.getRequest().get("url"));
        ObjectMapper mapper = new ObjectMapper();
        ResponseProxy response = new ResponseProxy();
        try {
            JsonNode jsonNode = mapper.readTree(httpClient.getResponse());
            response.setJsonResponse(jsonNode);
            response.setHttpStatusCode(httpClient.getStatusCode());
            response.setHttpStatusMessage(httpClient.getStatusMessage());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }
}
