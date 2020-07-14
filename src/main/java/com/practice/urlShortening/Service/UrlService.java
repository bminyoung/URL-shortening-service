package com.practice.urlShortening.Service;

import java.util.concurrent.CompletableFuture;

import com.practice.urlShortening.Url.Url;

public interface UrlService {
	
	public CompletableFuture<Url> getUrl(String url);
	
	public CompletableFuture<Long> registerUrl(Url url);
	
	public CompletableFuture<Integer> delete(String url);
}
