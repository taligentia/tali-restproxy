package com.taligentia.sharepointrestproxy.proxy;

import com.taligentia.base.bearer.model.AuthToken;

import java.util.List;

public class ProxyAuthList {
    private List<ProxyAuth> auth;

    public List<ProxyAuth> getAuth() {
        return auth;
    }

    public void setAuth(List<ProxyAuth> auth) {
        this.auth = auth;
    }

    public int size() {
        return auth.size();
    }

    public ProxyAuth get(int index) {
        return auth.get(index);
    }
}
