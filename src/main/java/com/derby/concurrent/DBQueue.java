package com.derby.concurrent;

import java.util.Map;
import java.util.List;
import com.derby.entity.Message;
import com.derby.handler.DBHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 数据入库队列
 * 
 * @author ZHANGYONG415
 *
 */
public class DBQueue {
	private static DBQueue dbQueue;
	private static ConcurrentLinkedDeque<Map<String, List<Message>>> linkedDeque = new ConcurrentLinkedDeque<Map<String, List<Message>>>();
	
	private DBQueue() {
		// 新起一个线程执行run方法(监听双端队列linkedDeque)
		new Thread(() -> run()).start();
	}
	
	public static synchronized DBQueue getInstance() {
		if (dbQueue == null) {
			dbQueue = new DBQueue();
		}
		return dbQueue;
	}
	
	/**
	 * 将消息实体添加到线程安全的非阻塞的双端队列
	 * @param map
	 */
	public void offer(Map<String, List<Message>> map) {
		linkedDeque.offer(map);
	}
	
	/**
	 * 从双端队列中获取消息实体执行
	 */
	public void run() {
		while (true) {
			while (linkedDeque.size() > 0) {
				Map<String, List<Message>> map = linkedDeque.poll();
				DBHandler dbHandler = new DBHandler();
				dbHandler.insertData(map);
			}
			
			try {
				// 休眠2秒
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
