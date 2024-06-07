package com.taligentia.sharepointrestproxy.proxy;

import com.taligentia.base.utils.Utils;

import javax.validation.constraints.NotNull;

public class ProxyAuth {

	@NotNull
	private String service;
	private String method;
	private String user;
	private String passwd;
	private String domain;

	public ProxyAuth() {}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = Utils.envReplacer(user);
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = Utils.envReplacer(passwd);
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = Utils.envReplacer(domain);
	}
}
