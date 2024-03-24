package com.taligentia.restproxy;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestProxyManagerConfiguration {
    @NotNull
    private int poolThreadSize;
    @NotNull
    private int retry;
    @NotNull
    private int timeOut;
    private String resources;
    
   public int getPoolThreadSize() {
		return poolThreadSize;
	}

	public void setPoolThreadSize(int poolThreadSize) {
		this.poolThreadSize = poolThreadSize;
	}

	public int getRetry() {
		return retry;
   }

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	@JsonProperty("resources")
	public String getResources() {
		return resources;
	}
    
}
