package com.practice.urlShortening.Service;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.practice.urlShortening.Converter.Converter;
import com.practice.urlShortening.Url.Url;
import com.practice.urlShortening.Url.UrlDAO;

import lombok.NonNull;

@Service
public class UrlServiceImpl implements UrlService{
	
	@Autowired
	private UrlDAO urlDAO;
	
	@Autowired
	private Converter conv;
	
	@Resource
	private UrlRedisServiceImpl redisService;
	
	/*
	 * return original url
	 * if fail, return null
	 * */
	@Override
	@Async("taskExecutor_get")
	public CompletableFuture<Url> getOriginalUrl(String shortUrl) {
		CompletableFuture<Url> urlInfo = redisService.getOriginalUrl(shortUrl);
		return urlInfo.thenApply(url -> {
			if(url == null) {
				long seq = conv.deconvert(shortUrl);
				url = urlDAO.getUrlBySeq(seq);				
			}
			return url;
		});
	}
	
	/*
	 * return index of row
	 * */
	@Override
	@Async("taskExecutor")
	public CompletableFuture<Long> registerUrl(Url url) {
		if(checkExist(url.getOriginalUrl())) {
			return CompletableFuture.completedFuture(-1L);
		}
		url.setShortUrl("-");
		
		urlDAO.register(url);
		String shortUrl = conv.shorten(url.getSeq());
		url.setShortUrl(shortUrl);
		url.setRegisterDate(new Date());
		
		// save cache
		CompletableFuture<Long> redisResult = redisService.registerUrl(url);
		
		urlDAO.update(url);
		return CompletableFuture.completedFuture(url.getSeq());
	}
	
	/*
	 * if url exist, return true
	 * */
	private boolean checkExist(@NonNull String originalUrl) {
		Integer num = urlDAO.originalExist(originalUrl);
		return num != null;
	}

	/*
	 * return result code
	 * */
	@Override
	@Async("taskExecutor")
	public CompletableFuture<Integer> delete(String shortUrl) {
		//delete cache
		redisService.delete(shortUrl);
		
		int result = urlDAO.delete(shortUrl);
		return CompletableFuture.completedFuture(result);
	}
	
}

