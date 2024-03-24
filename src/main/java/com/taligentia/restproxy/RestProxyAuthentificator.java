package com.taligentia.restproxy;

import java.util.Optional;

import org.jose4j.jwt.consumer.JwtContext;

import com.taligentia.base.bearer.BaseAuthenticator;
import com.taligentia.base.bearer.model.AuthUser;

import io.dropwizard.auth.Authenticator;

public class RestProxyAuthentificator extends BaseAuthenticator implements Authenticator<JwtContext, AuthUser> {
    public RestProxyAuthentificator(RestProxyManager manager) {
    	super(manager.getBearerManager());
    }

    @Override
    public Optional<AuthUser> authenticate(JwtContext context) {
        AuthUser user = getApiAuthToken().validateAuthToken(context);
        if (user!=null) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
