package com.taligentia.restproxy.model;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class UserContext {
	@NotNull
	String user;
	
	@NotNull
	List<String> editions;
	
	public UserContext() {
		editions = new ArrayList<String>();
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public List<String> getEditions() {
		return editions;
	}

	public void setEditions(List<String> editions) {
		this.editions = editions;
	}
}
