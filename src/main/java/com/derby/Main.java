package com.derby;

import java.util.Map;
import java.util.List;
import com.derby.entity.CompareResult;
import com.derby.entity.ResultMessage;
import com.derby.handler.MessageHandler;
import java.util.concurrent.ForkJoinPool;
import com.derby.concurrent.forkjoin.MessageForkJoinTask;

/**
 * 入口类
 * 
 * @author ZHANGYONG415
 *
 */
public class Main {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		
		MessageHandler handler = new MessageHandler();
		List<String> hotelCodeList = handler.getHotelCodeFromCSV();
		
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
//		System.out.println("test result:" + ResultMessage.getInstance().getResultFromTest());
		System.out.println("online size:" + ResultMessage.getInstance().getOnlineResultSize());
//		System.out.println("online result:" + ResultMessage.getInstance().getResultFromOnline());
		
		MessageHandler messageHandler = new MessageHandler();
		Map<String, List<CompareResult>> result = messageHandler.compareTestAndOnline(ResultMessage.getInstance().getResultFromTest(), 
																					  ResultMessage.getInstance().getResultFromOnline());
		
		System.out.println("compare different size:" + result.size());
		
		long end = System.currentTimeMillis();
		System.out.println("耗时:" + (end - start) + "毫秒;" + (end - start) * 1.0 / 1000 + "秒;" + (end - start) * 1.0 / 1000 / 60 + "分钟");
		
	}
}
