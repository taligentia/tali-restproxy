package com.taligentia.restproxy.proxy;

import com.codahale.metrics.health.HealthCheck;
import com.taligentia.base.dropwizard.BaseManager;
import com.taligentia.base.dropwizard.InfoManager;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;

public class ProxyManager implements Managed, BaseManager, InfoManager {

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
}
