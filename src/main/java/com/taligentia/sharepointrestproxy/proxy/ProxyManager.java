package com.taligentia.sharepointrestproxy.proxy;

import com.codahale.metrics.health.HealthCheck;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taligentia.base.dropwizard.BaseManager;
import com.taligentia.base.dropwizard.InfoManager;
import com.taligentia.sharepointrestproxy.model.QueryProxy;
import com.taligentia.sharepointrestproxy.model.QueryProxyDownload;
import com.taligentia.sharepointrestproxy.model.ResponseProxy;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;

public class ProxyManager implements Managed, BaseManager, InfoManager {
    public static final Logger logger = LoggerFactory.getLogger(ProxyManager.class);
    private ProxyConfiguration proxyConfiguration;
    private static final String ACCEPT_HEADER = "application/json;odata=verbose";
    private static final String ACCEPT_HEADER_PDF = "application/pdf";
    private static final String ACCEPT_HEADER_OCTETSTREAM = "application/octet-stream";
    private ProxyHttpClient httpClient;

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
        httpClient = new ProxyHttpClient();
        httpClient.setAcceptHeader(ACCEPT_HEADER);
        httpClient.setSslCertificateAuthorities(proxyConfiguration.getSslCertificateAuthorities());
        httpClient.setSslCertificateAuthoritiesPassword(proxyConfiguration.getSslCertificateAuthoritiesPassword());
        httpClient.setSslVerification(proxyConfiguration.getSslVerification());
        getLogger().debug("SharepointRestProxy : " + queryProxy.getRequest().get("url"));
        httpClient.doGet(proxyConfiguration.getAuth("sharepointrestproxy").getMethod(),proxyConfiguration.getAuth("sharepointrestproxy").getUser(), proxyConfiguration.getAuth("sharepointrestproxy").getPasswd(), proxyConfiguration.getAuth("sharepointrestproxy").getDomain(), queryProxy.getRequest().get("url"));
        ObjectMapper mapper = new ObjectMapper();
        ResponseProxy response = new ResponseProxy();
        try {
            JsonNode jsonNode = mapper.readTree(httpClient.getResponse());
            response.setJsonResponse(jsonNode);
            response.setHttpStatusCode(httpClient.getStatusCode());
            response.setHttpStatusMessage(httpClient.getStatusMessage());
            response.setContentType(httpClient.getContenType());
            response.setInputStream(httpClient.getInputStream());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public ResponseProxy download(QueryProxyDownload queryProxy) {
        httpClient = new ProxyHttpClient();
        httpClient.setAcceptHeader(ACCEPT_HEADER_OCTETSTREAM);
        httpClient.setSslCertificateAuthorities(proxyConfiguration.getSslCertificateAuthorities());
        httpClient.setSslCertificateAuthoritiesPassword(proxyConfiguration.getSslCertificateAuthoritiesPassword());
        httpClient.setSslVerification(proxyConfiguration.getSslVerification());
        getLogger().debug("SharepointRestProxy : " + queryProxy.getUrl());
        httpClient.doGet(proxyConfiguration.getAuth("sharepointrestproxy").getMethod(),proxyConfiguration.getAuth("sharepointrestproxy").getUser(), proxyConfiguration.getAuth("sharepointrestproxy").getPasswd(), proxyConfiguration.getAuth("sharepointrestproxy").getDomain(), queryProxy.getUrl());
        ResponseProxy response = new ResponseProxy();
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(httpClient.getResponse());
            response.setJsonResponse(jsonNode);
            response.setHttpStatusCode(httpClient.getStatusCode());
            response.setHttpStatusMessage(httpClient.getStatusMessage());
            response.setContentType(httpClient.getContenType());
            response.setInputStream(httpClient.getInputStream());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }

    public void close() {
        httpClient.close();
    }
}
