package com.practice.urlShortening.Url;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UrlDAO {
	
	public ArrayList<Url> getUrlList();
	
	public Url getUrl(String shortUrl);
	
	public Url getUrlBySeq(long seq);
	
	public int register(Url url);
	
	public int update(Url url);
	
	public int delete(String shortUrl);
	
	public Integer originalExist(String originalUrl);
}
