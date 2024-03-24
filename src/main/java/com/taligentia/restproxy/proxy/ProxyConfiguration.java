package com.taligentia.restproxy.proxy;

public class ProxyConfiguration {

    private String url;
    private String user;
    private String passwd;
    private String javaxSecurityAuthUseSubjectCredsOnly;
    private String sunSecurityKrb5Debug;
    private String javaSecurityKrb5Conf;
    private String javaSecurityAuthLoginConfig;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getJavaxSecurityAuthUseSubjectCredsOnly() {
        return javaxSecurityAuthUseSubjectCredsOnly;
    }

    public void setJavaxSecurityAuthUseSubjectCredsOnly(String javaxSecurityAuthUseSubjectCredsOnly) {
        this.javaxSecurityAuthUseSubjectCredsOnly = javaxSecurityAuthUseSubjectCredsOnly;
    }

    public String getSunSecurityKrb5Debug() {
        return sunSecurityKrb5Debug;
    }

    public void setSunSecurityKrb5Debug(String sunSecurityKrb5Debug) {
        this.sunSecurityKrb5Debug = sunSecurityKrb5Debug;
    }

    public String getJavaSecurityKrb5Conf() {
        return javaSecurityKrb5Conf;
    }

    public void setJavaSecurityKrb5Conf(String javaSecurityKrb5Conf) {
        this.javaSecurityKrb5Conf = javaSecurityKrb5Conf;
    }

    public String getJavaSecurityAuthLoginConfig() {
        return javaSecurityAuthLoginConfig;
    }

    public void setJavaSecurityAuthLoginConfig(String javaSecurityAuthLoginConfig) {
        this.javaSecurityAuthLoginConfig = javaSecurityAuthLoginConfig;
    }
}
