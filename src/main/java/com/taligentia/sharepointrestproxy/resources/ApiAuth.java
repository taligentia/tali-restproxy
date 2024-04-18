package com.taligentia.sharepointrestproxy.resources;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.google.common.collect.ImmutableMap;
import com.taligentia.base.bearer.ApiAuthToken;
import com.taligentia.base.bearer.model.AccessToken;
import com.taligentia.base.bearer.model.AuthToken;
import com.taligentia.base.bearer.model.AuthUser;
import com.taligentia.sharepointrestproxy.SharepointRestProxyManager;

import io.dropwizard.auth.Auth;
import io.swagger.v3.oas.annotations.Operation;

@Path("api/auth")
@Produces(MediaType.APPLICATION_JSON)
public class ApiAuth extends RestProxyRessource {
	static public String[] Pages = { "api/auth/.*" };
	
	final ApiAuthToken apiAuthToken;

	public ApiAuth(SharepointRestProxyManager manager) {
    	super(manager);
    	apiAuthToken = new ApiAuthToken(manager.getBearerManager());
    }

    @POST
    @Path("/token")
	@Operation(summary = "Get auth Token", tags = "auth")
	public Response getToken(AuthToken authToken) throws IOException {
    	BaseResponse<AccessToken> rep = start("getToken");
		try {
			AccessToken accessToken = apiAuthToken.getAuthToken(authToken);
			NewCookie cookie = apiAuthToken.getRefreshTokenCookie(authToken, "/api/auth", "");
			return response(rep, accessToken, cookie);
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}

	@GET
	@Path("/refresh")
	@Operation(summary = "Refesh auth Token", tags = "auth")
	public Response getRefreshToken(@CookieParam("refreshToken") String jwt) throws IOException {
		BaseResponse<AccessToken> rep = start("getRefreshToken");

		try {
			AuthToken authToken = apiAuthToken.buildAuthTokenFromJWT(jwt);
			AccessToken accessToken = apiAuthToken.getAuthToken(authToken);
			NewCookie cookie = apiAuthToken.getRefreshTokenCookie(authToken, "/api/auth", "");
			return response(rep, accessToken, cookie);
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}

	@GET
	@Path("/check-token")
	@Operation(summary = "Check auth Token", tags = "auth")
	public Map<String, Object> checkToken(@Auth Principal user) {
		return ImmutableMap.<String, Object>of("username", user.getName(), "id", ((AuthUser) user).getId());
	}
}
