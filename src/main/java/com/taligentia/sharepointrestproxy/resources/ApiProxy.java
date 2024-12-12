package com.taligentia.sharepointrestproxy.resources;

import java.security.Principal;
import java.util.Arrays;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import com.taligentia.base.dropwizard.utils.Utils;
import com.taligentia.sharepointrestproxy.SharepointRestProxyManager;
import com.taligentia.sharepointrestproxy.model.QueryProxy;
import com.taligentia.sharepointrestproxy.model.QueryProxyDownload;
import com.taligentia.sharepointrestproxy.model.ResponseProxy;
import com.taligentia.base.bearer.model.AuthUser;
import com.taligentia.base.bearer.model.InvalidRolesException;

import com.taligentia.sharepointrestproxy.utils.RestProxyUtils;
import io.dropwizard.auth.Auth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.StringUtils;

@Path("api/proxy")
public class ApiProxy extends RestProxyRessource {
	static public String[] Pages = { "api/proxy/.*" };

	public ApiProxy(SharepointRestProxyManager mainManager) {
		super(mainManager);
	}

	static private final String[] defaultExpectedRoles = { "read" };

	@POST
	@Path("process")
	@Operation(summary = "Process ", tags = "process")
	@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ResponseProxy.class)))
	public Response process(
			QueryProxy query,
			@Auth Principal user) {
		BaseResponse<ResponseProxy> rep = start("get");
		try {
			if (!AuthUser.userRolesMatch(getUserRoles((AuthUser) user), Arrays.asList(defaultExpectedRoles))) {
				return error(rep, new InvalidRolesException(Arrays.asList(defaultExpectedRoles), getUserRoles((AuthUser) user)));
			}
			ResponseProxy responseProxy = getRestProxyManager().process(query, false);
			String dumpDirectory = getRestProxyManager().getProxyManager().getProxyConfiguration().getDumpDirectory();
			if (responseProxy.getResponse()!=null && StringUtils.startsWith(responseProxy.getContentType(), "application/json") && StringUtils.isNotEmpty(dumpDirectory)) {
				String dumpValue = RestProxyUtils.prettyPrintJsonString(responseProxy.getResponse());
				RestProxyUtils.dumpToTextFile(dumpDirectory, "api_proxy.json", dumpValue);
			}
			responseProxy.setInputStream(null);
			getRestProxyManager().close();
			return response(rep, responseProxy);
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}
	@POST
	@Path("download")
	@Operation(summary = "Download ", tags = "download")
	@ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = ResponseProxy.class)))
	public Response download(
			QueryProxyDownload query,
			@Auth Principal user) {
		BaseResponse<ResponseProxy> rep = start("get");
		try {
			if (!AuthUser.userRolesMatch(getUserRoles((AuthUser) user), Arrays.asList(defaultExpectedRoles))) {
				return error(rep, new InvalidRolesException(Arrays.asList(defaultExpectedRoles), getUserRoles((AuthUser) user)));
			}
			ResponseProxy responseProxy = getRestProxyManager().process(query.asQueryProxy(), true);
			StreamingOutput streamingOutput = Utils.inputStreamStreaming(responseProxy.getInputStream());
			Response resp = Utils.pdf( streamingOutput );
			return resp;
		} catch (Exception ex) {
			return error(rep, ex);
		}
	}
}
