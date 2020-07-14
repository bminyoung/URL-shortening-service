package com.practice.urlShortening.Url;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Url{
	private long seq;
	private String shortUrl;
	@NonNull private String originalUrl;
	private Date registerDate;
}
