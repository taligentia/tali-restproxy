package com.taligentia.restproxy.resources;

import java.security.Principal;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.taligentia.restproxy.RestProxyManager;
import com.taligentia.restproxy.model.QueryProxy;
import com.taligentia.restproxy.model.ResponseProxy;

import io.dropwizard.auth.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Path("api/data")
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(name = "searchSecurity", type = SecuritySchemeType.APIKEY, 
	flows = @OAuthFlows(
		implicit = @OAuthFlow(authorizationUrl = "http://url.com/api/auth",
			scopes = @OAuthScope(name = "search:read:", description = "search and read datas")
		)
	)
)
public class ApiProxy extends RestProxyRessource {
	static public String[] Pages = { "api/data/.*" };

	public ApiProxy(RestProxyManager mainManager) {
		super(mainManager);
	}

	@POST
	@Path("search")
	@Operation(summary = "Search ", tags = "search")
	@SecurityRequirement(name = "searchSecurity", scopes = "search:read")
	@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ResponseProxy.class)))
	public Response search(
			QueryProxy query,
			@Auth Principal user) {
		BaseResponse<ResponseProxy> rep = start("");
		try {
			//return response(rep, getCoreManager().search(query));
			return null;
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}
}
