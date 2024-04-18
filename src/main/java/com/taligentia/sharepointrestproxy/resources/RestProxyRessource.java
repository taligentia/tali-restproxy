package com.taligentia.sharepointrestproxy.resources;

import com.taligentia.base.bearer.model.AuthUser;
import com.taligentia.base.dropwizard.ressources.BaseRessource;
import com.taligentia.sharepointrestproxy.SharepointRestProxyManager;

import java.util.List;

public class RestProxyRessource extends BaseRessource {

	protected RestProxyRessource(SharepointRestProxyManager mainManager) {
		super(mainManager);
	}

	protected SharepointRestProxyManager getRestProxyManager() {
		return (SharepointRestProxyManager) super.getManager();
	}
	
	protected <T> BaseResponse<T> start(String call) {
		return start(call, null, true);
	}

	protected List<String> getUserRoles(AuthUser user) {
		return getRestProxyManager().getBearerManager().getConfiguration().getAuthorization((user.getId())).getClientRoles();
	}
}
