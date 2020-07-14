package com.practice.urlShortening;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.practice.urlShortening.Controller.UrlController;
import com.practice.urlShortening.Service.UrlService;
import com.practice.urlShortening.Url.Url;

@SpringBootTest
class UrlShorteningApplicationTests {
	@Autowired
	private UrlService urlService;
	
	@Autowired
	private UrlController urlController;
	
	private MockMvc mockMvc;
	
	@Before
	public void createController() {
		mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
	}
    
    @Test
	void contextLoads() throws Exception {
//		CompletableFuture<Url> urlInfo = urlService.getUrl("test");
//		Url url = urlInfo.join();
//		assertEquals("test_original", url.getOriginalUrl());
    	RequestBuilder reqBuilder = MockMvcRequestBuilders.get("/urls/2").contentType(MediaType.APPLICATION_JSON);
    	mockMvc.perform(reqBuilder).andExpect(status().isOk()).andDo(print());
	}

}
