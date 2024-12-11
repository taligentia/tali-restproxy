package com.taligentia.sharepointrestproxy.proxy;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Arrays;
import javax.net.ssl.SSLContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyHttpClient {
    public static final Logger logger = LoggerFactory.getLogger(ProxyHttpClient.class);

    // https://stackoverflow.com/questions/21629132/httpclient-set-credentials-for-kerberos-authentication
    private String acceptHeader = null;
    private String response;
    private int statusCode;
    private String contenType;
    private String statusMessage;
    private String sslCertificateAuthorities = null;
    private String sslCertificateAuthoritiesPassword = null;
    private Boolean sslVerification;
    private InputStream inputStream;
    private HttpClient httpClient;

    public ProxyHttpClient() {
        sslVerification = true;
    }

    public void doGet(String authMethod, String user, String password, String domain, final String url) {
        logger.debug(String.format("SharepointRestProxy : Start doGet -> %s - %s - %s - %s", authMethod, user, password, domain));
        ConnectionSocketFactory sslsf = null;
        SSLContext sslContext = null;
        try {
            logger.debug(String.format("SharepointRestProxy : doGet -> sslVerification = %s ", sslVerification));
            if (!sslVerification) {
                TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
                sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            } else {
                if (StringUtils.isNotEmpty(sslCertificateAuthorities)) {
                    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                    FileInputStream in = new FileInputStream(sslCertificateAuthorities);
                    byte[] certificate = IOUtils.toByteArray(in);
                    ByteArrayInputStream bis = new ByteArrayInputStream(certificate);
                    if (StringUtils.isNotEmpty(sslCertificateAuthoritiesPassword))
                        trustStore.load(bis, sslCertificateAuthoritiesPassword.toCharArray());
                    sslContext = SSLContexts.custom()
                            .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                            .build();
                } else {
                    sslContext = SSLContexts.custom().loadTrustMaterial(null).build();
                }
            }
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

        Registry<ConnectionSocketFactory> socketFactoryRegistry =
                RegistryBuilder.<ConnectionSocketFactory> create()
                        .register("https", sslsf)
                        .register("http", new PlainConnectionSocketFactory())
                        .build();

        BasicHttpClientConnectionManager connectionManager =
                new BasicHttpClientConnectionManager(socketFactoryRegistry);

        // No authentication
        if (StringUtils.isEmpty(authMethod) || authMethod.equals("basic") || authMethod.equals("ntlm")) {
            try {

                final BasicCredentialsProvider provider = new BasicCredentialsProvider();
                CloseableHttpClient httpClient = null;

                if ("basic".equals(authMethod) && StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(password)) {
                    provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, password));

                    httpClient = HttpClients.custom()
                            .setConnectionManager(connectionManager)
                            .setDefaultCredentialsProvider(provider)
                            .build();
                }

                if ("ntlm".equals(authMethod) && StringUtils.isNotEmpty(user) && StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(domain)) {
                    provider.setCredentials(AuthScope.ANY, new NTCredentials(user, password, null, domain));
                    Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                            .register(AuthSchemes.NTLM, new NTLMSchemeFactory())
                            .build();

                    RequestConfig config = RequestConfig.custom()
                            .setConnectTimeout(10 * 1000)
                            .setConnectionRequestTimeout(10 * 1000)
                            .setCookieSpec(CookieSpecs.DEFAULT)
                            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.KERBEROS, AuthSchemes.SPNEGO))
                            .build();

                    httpClient = HttpClients.custom()
                            .setConnectionManager(connectionManager)
                            .setDefaultCredentialsProvider(provider)
                            .setDefaultRequestConfig(config)
                            .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                            .build();
                }

                if (httpClient==null)
                    httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

                call (httpClient, url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        // Kerberos authentication
        if (authMethod.equals("kerberos")) {
            try {
                /*
                "KrbLogin" name must match login.conf file entry

                KrbLogin {
                    com.sun.security.auth.module.Krb5LoginModule required
                    doNotPrompt=false
                    debug=true
                    useTicketCache=false
                    useKeyTab=true
                    principal="spadmin@TALIWIN.COM"
                    keyTab="/projects/Taligentia/CEA-SharepointRestProxy/sharepointrestproxy/src/test/resources/sharepoint.keytab";
                };

                https://docs.oracle.com/javase/8/docs/technotes/guides/security/jgss/tutorials/AcnOnly.html
                https://docs.oracle.com/javase/8/docs/jre/api/security/jaas/spec/com/sun/security/auth/module/Krb5LoginModule.html
                https://docs.oracle.com/javase/8/docs/api/javax/security/auth/login/LoginContext.html
                https://github.com/ymartin59/java-kerberos-sfudemo/blob/master/src/main/java/sfudemo/KerberosDemo.java
                */

                LoginContext loginContext = new LoginContext("KrbLogin", new KerberosCallBackHandler(user, password));
                loginContext.login();

                PrivilegedAction sendAction = new PrivilegedAction() {
                    @Override
                    public Object run() {
                        try {
                            Credentials use_jaas_creds = new Credentials() {
                                public String getPassword() {
                                    return null;
                                }
                                public Principal getUserPrincipal() {
                                    return null;
                                }
                            };

                            CredentialsProvider credsProvider = new BasicCredentialsProvider();
                            credsProvider.setCredentials(new AuthScope(null, -1, null), use_jaas_creds);
                            // Désactivation de la résolution DNS inverse
                            // https://stackoverflow.com/a/48265900
                            Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true, false)).build();

                            HttpClient httpClient = HttpClients.custom()
                                    .setConnectionManager(connectionManager)
                                    .setDefaultCredentialsProvider(credsProvider)
                                    .setDefaultAuthSchemeRegistry(authSchemeRegistry)
                                    .build();

                            call(httpClient, url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                };
                Subject.doAs(loginContext.getSubject(), sendAction);
            } catch (LoginException le) {
                le.printStackTrace();
            }
        }
    }

    private void call(HttpClient httpClient, String url) throws IOException {

        try {
            this.httpClient = httpClient;
            HttpUriRequest request = new HttpGet(url);
            if (!StringUtils.isEmpty(this.acceptHeader))
                request.setHeader("Accept", this.acceptHeader);
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity entity = httpResponse.getEntity();
            this.statusCode = httpResponse.getStatusLine().getStatusCode();
            this.statusMessage = httpResponse.getStatusLine().getReasonPhrase();
            this.contenType = entity.getContentType().getValue();
            if (!StringUtils.startsWith(this.contenType, "application/octet-stream") && !StringUtils.startsWith(this.contenType, "application/pdf")) {
                this.response = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            }
            else {
                this.response = "";
                this.inputStream = entity.getContent();
            }
        } finally {
            //httpClient.getConnectionManager().shutdown();
        }
    }

    void close() {
        httpClient.getConnectionManager().shutdown();
    }

    class KerberosCallBackHandler implements CallbackHandler {

        private final String user;
        private final String password;

        public KerberosCallBackHandler(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    logger.debug(String.format("SharepointRestProxy : KerberosCallBackHandler NameCallback"));
                    NameCallback nc = (NameCallback) callback;
                    nc.setName(user);
                } else if (callback instanceof PasswordCallback) {
                    logger.debug(String.format("SharepointRestProxy : KerberosCallBackHandler PasswordCallback"));
                    PasswordCallback pc = (PasswordCallback) callback;
                    pc.setPassword(password.toCharArray());
                } else {
                    logger.debug(String.format("SharepointRestProxy : KerberosCallBackHandler Unknown Callback : %s ", callback.toString()));
                    throw new UnsupportedCallbackException(callback, "Unknown Callback");
                }
            }
        }
    }

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    public void setSslVerification(Boolean sslVerification) {
        this.sslVerification = sslVerification;
    }

    public void setSslCertificateAuthorities(String sslCertificateAuthorities) {
        this.sslCertificateAuthorities = sslCertificateAuthorities;
    }

    public void setSslCertificateAuthoritiesPassword(String sslCertificateAuthoritiesPassword) {
        this.sslCertificateAuthoritiesPassword = sslCertificateAuthoritiesPassword;
    }

    public String getResponse() {
        return response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getContenType() {
        return contenType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}