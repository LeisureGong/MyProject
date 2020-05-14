package com.leisure.myproject.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * 异步线程调用
 * @author gonglei
 * @date 2020/5/14
 */
@Component
@Slf4j
public class AsyncTask {
	@Async("mySimpleAsync")
	public Future<String> doTask1() throws InterruptedException{
		log.info("Task1 started...");
		long start = System.currentTimeMillis();
		Thread.sleep(5000);
		long end  = System.currentTimeMillis();
		log.info("Task1 finished,time elapsed : {} ms",end - start);
		return new AsyncResult<>("Task1 accomplished");
	}


	@Async("myAsync")
	public Future<String> doTask2() throws InterruptedException{
		log.info("Task2 started...");
		long start = System.currentTimeMillis();
		Thread.sleep(3000);
		long end  = System.currentTimeMillis();
		log.info("Task2 finished,time elapsed : {} ms",end - start);
		return new AsyncResult<>("Task2 accomplished");
	}
}
