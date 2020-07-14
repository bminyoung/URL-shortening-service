package com.practice.urlShortening.Response;

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.practice.urlShortening.Response.UrlResponse.ResultCode;

public class ErrorResponse{
	private final String error;
	private final String message;
	private final ResultCode code;
	private String timestamp;
	
	@JsonCreator
	public ErrorResponse(@JsonProperty("error") String error,
						 @JsonProperty("message") String message,
						 @JsonProperty("code") ResultCode code,
						 @JsonProperty("timestamp") String timestamp) {
		this.error = requireNonNull(error, "error");
        this.message = requireNonNull(message, "message");
        this.code = code;
        this.timestamp = requireNonNull(timestamp, "timestamp");
	}
	
	@JsonProperty
	public String error() {
		return error;
	}
	
	@JsonProperty
	public String message() {
		return message;
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
