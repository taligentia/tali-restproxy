package com.taligentia.restproxy.proxy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.X509TrustManager;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.security.*;
import java.util.Set;
import javax.net.ssl.TrustManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;
import java.time.Duration;
import org.apache.http.conn.ssl.SSLContexts
;

public class HttpClientKerberos {
    // https://stackoverflow.com/questions/21629132/httpclient-set-credentials-for-kerberos-authentication

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    private String acceptHeader = null;

    private String response;

    public void doGet(String user, String password, final String url) {
        if (user==null || "".equals(user) || password==null || "".equals(password)) {

            try {
            TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory> create()
                            .register("https", sslsf)
                            .register("http", new PlainConnectionSocketFactory())
                            .build();

            BasicHttpClientConnectionManager connectionManager =
                    new BasicHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                    .setConnectionManager(connectionManager).build();

                call (httpClient, url);
            } catch (IOException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
                e.printStackTrace();
            }
            return;
        }
        try {
            // "KrbLogin" name must match login.conf file entry
            // KrbLogin{
            //     com.sun.security.auth.module.Krb5LoginModule required doNotPrompt=false debug=true useTicketCache=false;
            // };
            LoginContext loginContext = new LoginContext("KrbLogin", new KerberosCallBackHandler(user, password));
            loginContext.login();

            PrivilegedAction sendAction = new PrivilegedAction() {
                @Override
                public Object run() {
                    try {
                        Subject current = Subject.getSubject(AccessController.getContext());
                        //System.out.println("----------------------------------------");
                        Set<Principal> principals = current.getPrincipals();
                        //for (Principal next : principals) {
                        //    System.out.println("DOAS Principal: " + next.getName());
                        //}
                        //System.out.println("----------------------------------------");
                        HttpClient httpClient = getHttpClient();
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

    private void call(HttpClient httpclient, String url) throws IOException {

        try {
            HttpUriRequest request = new HttpGet(url);
            if (this.acceptHeader!=null && !"".equals(this.acceptHeader))
                request.setHeader("Accept",this.acceptHeader);
            HttpResponse httpResponse = httpclient.execute(request);
            HttpEntity entity = httpResponse.getEntity();

            //System.out.println("----------------------------------------");
            //System.out.println("STATUS >> " + response.getStatusLine());
            //if (entity != null) {
            //    System.out.println("RESULT >> " + EntityUtils.toString(entity));
            //}
            //System.out.println("----------------------------------------");

            this.response = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }

    private HttpClient getHttpClient() {
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
        Registry<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create().register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true)).build();
        return HttpClients.custom().setDefaultAuthSchemeRegistry(authSchemeRegistry).setDefaultCredentialsProvider(credsProvider).build();
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
                    NameCallback nc = (NameCallback) callback;
                    nc.setName(user);
                } else if (callback instanceof PasswordCallback) {
                    PasswordCallback pc = (PasswordCallback) callback;
                    pc.setPassword(password.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback, "Unknown Callback");
                }
            }
        }
    }

    public String getResponse() {
        return response;
    }
}