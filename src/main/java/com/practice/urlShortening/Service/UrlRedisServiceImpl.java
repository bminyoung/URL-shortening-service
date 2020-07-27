package com.practice.urlShortening.Service;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.urlShortening.Url.Url;

@Component
public class UrlRedisServiceImpl implements UrlService{
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	@Resource
	RedisTemplate<String, Object> redisTemplate;

	@Override
	public CompletableFuture<Url> getOriginalUrl(String shortUrl) {
		Url url = null;
		try {
//			Object result = redisTemplate.opsForValue().get(shortUrl);
//			objectMapper.convert
			
			url = (Url) redisTemplate.opsForValue().get(shortUrl);
//			url = (Url) redisTemplate.opsfor.get(shortUrl);
//			String str = (String) redisTemplate.opsForValue().get(shortUrl);
//			url = mapper.convertValue(redisTemplate.opsForValue().get(shortUrl), Url.class);
//			url = mapper.readValue(str, Url.class);
			logger.info("Redis : getOriginalUrl success");
		}catch (Exception e) {
			logger.error("Redis : getOriginalUrl fail " + e);
		}
		
		return CompletableFuture.completedFuture(url);
	}

	@Override
	public CompletableFuture<Long> registerUrl(Url url) {
		String key = url.getShortUrl();
		try {
			redisTemplate.opsForValue().set(key, url, Duration.ofHours(48));
			logger.info("Redis : registerUrl success");
		}catch (Exception e) {
			logger.error("Redis : registerUrl fail " + e);
		}
		
		return CompletableFuture.completedFuture(0L); // redis has no index
	}

	@Override
	public CompletableFuture<Integer> delete(String shortUrl) {
		try {
			redisTemplate.delete(shortUrl);
			logger.info("Redis : deleteUrl success");
		}catch (Exception e) {
			logger.error("Redis : deleteUrl fail " + e);
		}
		
		return CompletableFuture.completedFuture(1); // always return success
	}

}
