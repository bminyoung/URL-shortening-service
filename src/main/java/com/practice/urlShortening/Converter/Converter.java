package com.practice.urlShortening.Converter;

public interface Converter {
	String shorten(String str);
	
	String shorten(long num);
	
	long deconvert(String str);
}
