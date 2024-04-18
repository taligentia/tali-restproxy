package com.taligentia.sharepointrestproxy.model;

import com.taligentia.sharepointrestproxy.SharepointRestProxyManager;

public class ResponseInfoVersion {
    private SharepointRestProxyManager manager;

    public ResponseInfoVersion(SharepointRestProxyManager manager) {
        this.manager = manager;
    }
    public String getVersion() {
        return manager.getVersion();
    }

}
