package com.practice.urlShortening.Converter;

import org.springframework.stereotype.Component;

@Component
public class UrlConverter implements Converter{
	private final String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	@Override
	public String shorten(String str) {
		return str;
	}
	
	@Override
	public String shorten(long num) {
		char[] ch = str.toCharArray();
		String res = "";
		while(num > 0) {
			res += ch[(int)(num % 62)];
			num /= 62;
		}
		// convert str to str (not yet)
		return res;
	}

	@Override
	public long deconvert(String shortUrl) {
		char[] ch = shortUrl.toCharArray();
		long unit = 1;
		long ret = 0;
		for (int i = shortUrl.length()-1; i >= 0; i--) {
			ret += str.indexOf(shortUrl.charAt(i)) * unit;
			unit *= 62;
		}
		
		return ret;
	}
	
}
