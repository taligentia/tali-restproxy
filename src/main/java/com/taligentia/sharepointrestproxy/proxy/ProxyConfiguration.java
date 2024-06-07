package com.taligentia.sharepointrestproxy.proxy;

import com.taligentia.base.bearer.model.AuthToken;
import com.taligentia.base.dropwizard.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProxyConfiguration {
    private String authFile;
    private ProxyAuthList proxyAuths;

    private String javaxSecurityAuthUseSubjectCredsOnly;
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
        proxyAuths = Utils.fromYaml(f, ProxyAuthList.class);
    }

    public ProxyAuth getAuth(String service) {
        if (proxyAuths!=null)
            for (int i=0; i<proxyAuths.size(); i++)
                if (service.equals(proxyAuths.get(i).getService()))
                    return proxyAuths.get(i);
        return null;
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

    public String getDumpDirectory() {
        return dumpDirectory;
    }

    public void setDumpDirectory(String dumpDirectory) {
        this.dumpDirectory = dumpDirectory;
    }
}
