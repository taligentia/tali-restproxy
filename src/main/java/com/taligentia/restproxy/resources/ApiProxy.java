package com.taligentia.restproxy.resources;

import java.security.Principal;
import java.util.Arrays;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.taligentia.restproxy.RestProxyManager;
import com.taligentia.restproxy.model.QueryProxy;
import com.taligentia.restproxy.model.ResponseProxy;
import com.taligentia.base.bearer.model.AuthUser;
import com.taligentia.base.bearer.model.InvalidRolesException;

import io.dropwizard.auth.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("api/proxy")
@Produces(MediaType.APPLICATION_JSON)
public class ApiProxy extends RestProxyRessource {
	static public String[] Pages = { "api/proxy/.*" };

	public ApiProxy(RestProxyManager mainManager) {
		super(mainManager);
	}

	static private final String[] expectedRoles = { "read" };

	@POST
	@Path("process")
	@Operation(summary = "Process ", tags = "process")
	@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ResponseProxy.class)))
	public Response process(
			QueryProxy query,
			@Auth Principal user) {
		BaseResponse<ResponseProxy> rep = start("get");
		try {
			if (!AuthUser.userRolesMatchOrNull(getUserRoles((AuthUser) user), Arrays.asList(expectedRoles)))
				return error(rep, new InvalidRolesException(Arrays.asList(expectedRoles), getUserRoles((AuthUser) user)));
			return response(rep, getRestProxyManager().process(query));
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}
}
