package com.practice.urlShortening.Configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig{
	
	/* basic size */
	private static int TASK_CORE_POOL_SIZE = 3;
	/* max size */
    private static int TASK_MAX_POOL_SIZE = 10;
    /* additional waiting queue size */
    private static int TASK_QUEUE_CAPACITY = 5;
    /* prefix + thread_num */
    private static String TASK_NAME_PREFIX = "Async-";

	
	@Bean(name = "taskExecutor_get")
	public Executor threadPoolTaskExecutor_get() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(TASK_CORE_POOL_SIZE);
		taskExecutor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
		taskExecutor.setQueueCapacity(TASK_QUEUE_CAPACITY);
		taskExecutor.setThreadNamePrefix(TASK_NAME_PREFIX + "get-");
		taskExecutor.initialize();
		return taskExecutor;
	}
	
	@Bean(name = "taskExecutor")
	public Executor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(TASK_CORE_POOL_SIZE);
		taskExecutor.setMaxPoolSize(TASK_MAX_POOL_SIZE);
		taskExecutor.setQueueCapacity(TASK_QUEUE_CAPACITY);
		taskExecutor.setThreadNamePrefix(TASK_NAME_PREFIX);
		taskExecutor.initialize();
		return taskExecutor;
	}

}
