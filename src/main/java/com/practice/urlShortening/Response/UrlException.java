package com.practice.urlShortening.Response;

import org.springframework.web.bind.annotation.ResponseStatus;

public class UrlException {
	@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "NotExist")
	public static class NotExistException extends RuntimeException {
		public NotExistException(String message) {
			super(message);
		}
	}
	
	@ResponseStatus(value = org.springframework.http.HttpStatus.UNAUTHORIZED, reason = "Unauthorized")
	public static class UnauthorizedException extends RuntimeException {
	  public UnauthorizedException(String message) {
	    super(message);
	  }
	}
	
	@ResponseStatus(value = org.springframework.http.HttpStatus.BAD_REQUEST, reason = "Duplicated")
	public static class DuplicatedException extends RuntimeException {
	  public DuplicatedException(String message) {
	    super(message);
	  }
	}
	
	@ResponseStatus(value = org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, reason = "unknown")
	public static class OtherException extends RuntimeException {
	  public OtherException(String message) {
	    super(message);
	  }
	}
}
