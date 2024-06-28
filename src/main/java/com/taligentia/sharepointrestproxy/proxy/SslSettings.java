package com.taligentia.sharepointrestproxy.proxy;

public class SslSettings {
    private String sslCertificateAuthorities;
    private Boolean sslVerification;

    public SslSettings() {}

    public String getSslCertificateAuthorities() {
        return sslCertificateAuthorities;
    }

    public void setSslCertificateAuthorities(String sslCertificateAuthorities) {
        this.sslCertificateAuthorities = sslCertificateAuthorities;
    }

    public Boolean getSslVerification() {
        return sslVerification;
    }

    public void setSslVerification(Boolean sslVerification) {
        this.sslVerification = sslVerification;
    }
}
