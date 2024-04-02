package com.taligentia.restproxy.resources;

import com.taligentia.base.bearer.model.AuthUser;
import com.taligentia.base.dropwizard.ressources.BaseRessource;
import com.taligentia.restproxy.RestProxyManager;

import java.util.List;

public class RestProxyRessource extends BaseRessource {

	protected RestProxyRessource(RestProxyManager mainManager) {
		super(mainManager);
	}

	protected RestProxyManager getRestProxyManager() {
		return (RestProxyManager) super.getManager();
	}
	
	protected <T> BaseResponse<T> start(String call) {
		return start(call, null, true);
	}

	protected List<String> getUserRoles(AuthUser user) {
		return getRestProxyManager().getBearerManager().getConfiguration().getAuthorization((user.getId())).getClientRoles();
	}
}
