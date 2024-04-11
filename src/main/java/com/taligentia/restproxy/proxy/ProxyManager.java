package com.taligentia.restproxy.proxy;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taligentia.base.dropwizard.BaseManager;
import com.taligentia.base.dropwizard.InfoManager;
import com.taligentia.restproxy.model.QueryProxy;
import com.taligentia.restproxy.model.ResponseProxy;
import com.taligentia.restproxy.utils.Utils;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;


import java.io.IOException;

public class ProxyManager implements Managed, BaseManager, InfoManager {

    private HttpClientKerberos httpClient ;
    private ProxyConfiguration proxyConfiguration;

    public ProxyManager(HttpClientKerberos httpClient, ProxyConfiguration proxyConfiguration) throws IllegalArgumentException, ClassNotFoundException, IOException {
        this.httpClient = httpClient;
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
        return null;
    }

    @Override
    public void start() throws Exception {
    }

    @Override
    public void stop() throws Exception {
    }

    public ResponseProxy process(QueryProxy queryProxy) {
        httpClient.setAcceptHeader(queryProxy.getAcceptHeader());
        httpClient.doGet(proxyConfiguration.getUser(),proxyConfiguration.getPasswd(),queryProxy.getRequest().get("url"));
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
