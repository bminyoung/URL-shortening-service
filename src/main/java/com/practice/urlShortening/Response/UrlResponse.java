package com.practice.urlShortening.Response;

/*
 * class of object
 * */

public class UrlResponse {
	public enum ResultCode{
		SUCCESS,
		STORAGE_FAIL,
		DUPLICATED,
		NOT_FOUND,
		OTHER_ERRORS,
	}
}
