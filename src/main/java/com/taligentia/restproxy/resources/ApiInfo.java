package com.taligentia.restproxy.resources;

import java.security.Principal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.taligentia.base.dropwizard.ManagerInfo;
import com.taligentia.restproxy.RestProxyManager;
import com.taligentia.restproxy.model.ResponseInfoVersion;

import io.dropwizard.auth.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("api/info")
@Produces(MediaType.APPLICATION_JSON)
public class ApiInfo extends RestProxyRessource {
	static public String[] Pages = { "api/info/.*" };

	public ApiInfo(RestProxyManager mainManager) {
		super(mainManager);
	}

	@GET
	@Path("/version")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get version", tags = "info version")
	@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseInfoVersion.class)))
	public Response version() {
		BaseResponse<ResponseInfoVersion> rep = start("version");
		try {
			return response(rep, getRestProxyManager().getInfoVersion());
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}

	@GET
	@Path("/status")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get status", tags = "info status")
	@ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ManagerInfo.class)))
	public Response status(@Auth Principal user) {
		BaseResponse<ManagerInfo> rep = start("status");
		try {
			getLogger().debug( user.getName() );
			return response(rep, getRestProxyManager().getMangerInfo());
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}

}
