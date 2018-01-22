package com.derby;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import com.derby.entity.CompareResult;
import com.derby.entity.ResultMessage;
import com.derby.handler.MessageHandler;
import java.util.concurrent.ForkJoinPool;
import com.derby.concurrent.forkjoin.MessageForkJoinTask;

/**
 * Èë¿ÚÀà
 * 
 * @author ZHANGYONG415
 *
 */
public class Main {
	public static void main(String[] args) {
		List<String> hotelCodeList = Arrays.asList("STAR-1", "STAR-2", "STAR-3", "STAR-4", "STAR-5");
		
		ForkJoinPool forkJoinPool = new ForkJoinPool();
		MessageForkJoinTask messageForkJoinTask = new MessageForkJoinTask(hotelCodeList, 0, hotelCodeList.size());
		forkJoinPool.execute(messageForkJoinTask);
		
		int testResultSize;
		int onlineResultSize;
		do {
			try {
				System.out.println("please wait 20 second");
				Thread.sleep(20 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			testResultSize = ResultMessage.getInstance().getTestResultSize();
			onlineResultSize = ResultMessage.getInstance().getOnlineResultSize();
		} while (testResultSize != hotelCodeList.size() || onlineResultSize != hotelCodeList.size());
		
		System.out.println("test size:" + ResultMessage.getInstance().getTestResultSize());
		System.out.println("test result:" + ResultMessage.getInstance().getResultFromTest());
		System.out.println("online size:" + ResultMessage.getInstance().getOnlineResultSize());
		System.out.println("online result:" + ResultMessage.getInstance().getResultFromOnline());
		
		MessageHandler messageHandler = new MessageHandler();
		Map<String, List<CompareResult>> result = messageHandler.compareTestAndOnline(ResultMessage.getInstance().getResultFromTest(), 
																					  ResultMessage.getInstance().getResultFromOnline());
		
		System.out.println("compare different size:" + result.size());
		
	}
}
