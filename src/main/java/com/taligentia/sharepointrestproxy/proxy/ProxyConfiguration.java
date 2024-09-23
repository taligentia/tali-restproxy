package com.taligentia.sharepointrestproxy.proxy;

import com.taligentia.base.dropwizard.utils.Utils;

import java.io.File;
import java.io.IOException;

public class ProxyConfiguration {
    private String authFile;
    private AuthLoginList proxyAuths;

    private String sslSettingsFile;
    private SslSettings sslSettings;

    private String javaxSecurityAuthUseSubjectCredsOnly;
    private String sunSecurityJgssDebug;
    private String sunSecuritySpnegoDebug;
    private String sunSecurityKrb5Debug;
    private String javaSecurityKrb5Conf;
    private String javaSecurityAuthLoginConfig;
    private String dumpDirectory;

    public String getAuthFile() {
        return authFile;
    }

    public void setAuthFile(String authFile) throws IOException {
        this.authFile = authFile;
        if (authFile==null)
            return;
        File f = new File(authFile);
        if (!f.exists())
            return;
        proxyAuths = Utils.fromYaml2(f, AuthLoginList.class);
    }

    public String getSslSettingsFile() {
        return sslSettingsFile;
    }

    public void setSslSettingsFile(String sslSettingsFile) throws IOException {
        this.sslSettingsFile = sslSettingsFile;
        if (sslSettingsFile==null)
            return;
        File f = new File(sslSettingsFile);
        if (!f.exists())
            return;
        sslSettings = Utils.fromYaml2(f, SslSettings.class);
    }

    public AuthLogin getAuth(String service) {
        if (proxyAuths!=null)
            for (int i=0; i<proxyAuths.size(); i++)
                if (service.equals(proxyAuths.get(i).getService()))
                    return proxyAuths.get(i);
        return null;
    }

    public SslSettings getSslSettings() {
        return sslSettings;
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

    public String getSunSecurityJgssDebug() {
        return sunSecurityJgssDebug;
    }

    public void setSunSecurityJgssDebug(String sunSecurityJgssDebug) {
        this.sunSecurityJgssDebug = sunSecurityJgssDebug;
    }

    public String getSunSecuritySpnegoDebug() {
        return sunSecuritySpnegoDebug;
    }

    public void setSunSecuritySpnegoDebug(String sunSecuritySpnegoDebug) {
        this.sunSecuritySpnegoDebug = sunSecuritySpnegoDebug;
    }

    public String getJavaSecurityAuthLoginConfig() {
        return javaSecurityAuthLoginConfig;
    }

    public void setJavaSecurityAuthLoginConfig(String javaSecurityAuthLoginConfig) {
        this.javaSecurityAuthLoginConfig = javaSecurityAuthLoginConfig;
    }

    public String getDumpDirectory() {
        return dumpDirectory;
    }

    public void setDumpDirectory(String dumpDirectory) {
        this.dumpDirectory = dumpDirectory;
    }

    public String getSslCertificateAuthorities() {
        return sslSettings.getSslCertificateAuthorities();
    }

    public String getSslCertificateAuthoritiesPassword() {
        return sslSettings.getSslCertificateAuthoritiesPassword();
    }

    public Boolean getSslVerification() {
        return sslSettings.getSslVerification();
    }

}
