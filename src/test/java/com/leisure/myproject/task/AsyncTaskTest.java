package com.leisure.myproject.task; 

import com.leisure.myproject.MyProjectApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/** 
* AsyncTask Tester. 
* 
* @author <Authors name> 
* @since <pre>5�� 14, 2020</pre> 
* @version 1.0 
*/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyProjectApplication.class)
public class AsyncTaskTest {

	@Autowired
	private AsyncTask asyncTask;

	@Test
	public void AsyncTaskTest() throws InterruptedException, ExecutionException {
		Future<String> task1 = asyncTask.doTask1();
		Future<String> task2 = asyncTask.doTask2();

		while(true){
			if(task1.isDone() && task2.isDone()){
				log.info("Task1 result: {}",task1.get());
				log.info("Task2 result: {}",task2.get());
				break;
			}
			Thread.sleep(1000);
		}
		log.info("All tasks finished");
	}



} 
