package com.taligentia.restproxy.model;

import com.taligentia.restproxy.RestProxyManager;

public class ResponseInfoVersion {
    private RestProxyManager manager;

    public ResponseInfoVersion(RestProxyManager manager) {
        this.manager = manager;
    }
    public String getVersion() {
        return manager.getVersion();
    }

}
