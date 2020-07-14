package com.practice.urlShortening.Response;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.urlShortening.Response.UrlResponse.ResultCode;

public class SuccessResponse{
	Object result = "";
	ResultCode code = ResultCode.SUCCESS;
	String timestamp = "";
	
	@JsonCreator
	public SuccessResponse(@JsonProperty("result") Object result,
						 @JsonProperty("code") ResultCode code,
						 @JsonProperty("timestamp") String timestamp) {
		this.result = result;
        this.code = code;
        this.timestamp = requireNonNull(timestamp, "timestamp");
	}
	
	@JsonProperty
	public Object result() {
		return result;
	}
	
	@JsonProperty
	public ResultCode code() {
		return code;
	}
	
	@JsonProperty
	public String timestamp() {
		return timestamp;
	}
}
