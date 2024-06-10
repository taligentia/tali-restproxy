package com.taligentia.sharepointrestproxy.proxy;

import java.util.List;

public class AuthLoginList {
    private List<AuthLogin> auth;

    public List<AuthLogin> getAuth() {
        return auth;
    }

    public void setAuth(List<AuthLogin> auth) {
        this.auth = auth;
    }

    public int size() {
        return auth.size();
    }

    public AuthLogin get(int index) {
        return auth.get(index);
    }
}
