package com.taligentia.sharepointrestproxy.model;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class QueryProxyDownload {
    @NotNull
    private String url;

    public QueryProxyDownload() {
    }

    public @NotNull String getUrl() {
        return url;
    }

    public void setUrl(@NotNull String url) {
        this.url = url;
    }

    public QueryProxy asQueryProxy () {
        QueryProxy query = new QueryProxy();
        Map<String, String> request = new HashMap<String, String>();
        request.put("url", url);
        query.setRequest(request);
        query.setDebug(false);
        return query;
    }
}
