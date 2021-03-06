package com.leisure.myproject.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Executor配置类
 * @author gonglei
 * @date 2020/5/14
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
	/** set the ThreadPoolExecutor's core pool size*/
	private int corePoolSize = 10;

	/** set the ThreadPoolExecutor's maximum pool size.*/
	private int maxPoolSize = 20;

	/** Set the capacity for the ThreadPoolExecutor's BlockingQueue.*/
	private int queueCapacity = 200;

	// 允许线程空闲时间
	private int keepAliveSeconds = 60;

	@Bean
	public Executor mySimpleAsync() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		// 线程池名前缀
		executor.setThreadNamePrefix("MySimpleExecutor-");
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor myAsync() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("MyExecutor - ");
		//rejection-policy: 当pool已经达到max size的时候，如何处理新任务
		//CALLER_RUNS: 不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();
		return executor;
	}
}
