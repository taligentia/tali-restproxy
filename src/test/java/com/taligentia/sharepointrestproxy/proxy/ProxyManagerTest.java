package com.taligentia.sharepointrestproxy.proxy;
import org.junit.Test;

public class ProxyManagerTest {
    @Test
    public void testDoGet() throws Exception {

        System.setProperty("java.security.auth.login.config", "src/test/resources/login.conf");
        System.setProperty("java.security.krb5.conf", "src/test/resources/krb5.conf");
        System.setProperty("sun.security.krb5.debug", "true");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");

        String authMethod = "kerberos";
        String user = "";
        String passwd = "";
        String domain = "TALIWIN";
        String url = "https://win2016-sp.taliwin.com/sites/kamare/_api/web/title";

        ProxyHttpClient httpClient = new ProxyHttpClient();
        httpClient.setAcceptHeader("application/json;odata=verbose");
        httpClient.setSslVerification(true);
        httpClient.setSslCertificateAuthorities("/projects/Taligentia/CEA-SharepointRestProxy/sharepointrestproxy/src/test/resources/win2016-sp.cer");
        httpClient.setSslCertificateAuthoritiesPassword(null);
        httpClient.doGet(authMethod, user, passwd, domain, url);
        System.out.println(httpClient.getResponse());
    }
}
