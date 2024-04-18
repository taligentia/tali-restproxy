package com.taligentia.sharepointrestproxy;

import java.util.Optional;

import org.jose4j.jwt.consumer.JwtContext;

import com.taligentia.base.bearer.BaseAuthenticator;
import com.taligentia.base.bearer.model.AuthUser;

import io.dropwizard.auth.Authenticator;

public class SharepointRestProxyAuthentificator extends BaseAuthenticator implements Authenticator<JwtContext, AuthUser> {
    public SharepointRestProxyAuthentificator(SharepointRestProxyManager manager) {
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
