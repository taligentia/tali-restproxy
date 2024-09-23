package com.taligentia.sharepointrestproxy.proxy;

public class SslSettings {
    private String sslCertificateAuthorities;
    private String sslCertificateAuthoritiesPassword;
    private Boolean sslVerification;

    public SslSettings() {}

    public String getSslCertificateAuthorities() {
        return sslCertificateAuthorities;
    }

    public void setSslCertificateAuthorities(String sslCertificateAuthorities) {
        this.sslCertificateAuthorities = sslCertificateAuthorities;
    }

    public String getSslCertificateAuthoritiesPassword() {
        return sslCertificateAuthoritiesPassword;
    }

    public void setSslCertificateAuthoritiesPassword(String sslCertificateAuthoritiesPassword) {
        this.sslCertificateAuthoritiesPassword = sslCertificateAuthoritiesPassword;
    }

    public Boolean getSslVerification() {
        return sslVerification;
    }

    public void setSslVerification(Boolean sslVerification) {
        this.sslVerification = sslVerification;
    }
}
