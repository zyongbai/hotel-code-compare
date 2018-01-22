package com.derby.concurrent;

import com.derby.entity.Message;
import com.derby.util.SystemMessage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 工作队列
 * 
 * @author ZHANGYONG415
 *
 */
public class WorkQueue {
	private static final Integer THREAD_NUM = Integer.parseInt(SystemMessage.getString("THREAD_NUM"));// 并发线程数
	
	private static WorkQueue workQueue;
	private static ConcurrentLinkedDeque<Message> linkedDeque = new ConcurrentLinkedDeque<Message>();
	
	private WorkQueue() {
		// 新起一个线程执行run方法(监听双端队列linkedDeque)
		new Thread(() -> run()).start();
	}
	
	public static synchronized WorkQueue getInstance() {
		if (workQueue == null) {
			workQueue = new WorkQueue();
		}
		return workQueue;
	}
	
	/**
	 * 将消息实体添加到线程安全的非阻塞的双端队列
	 * @param message
	 */
	public void offer(Message message) {
		linkedDeque.offer(message);
	}
	
	/**
	 * 从双端队列中获取消息实体执行
	 */
	public void run() {
		while (true) {
			while (linkedDeque.size() > 0) {
				// 先取双端队列中元素个数，防止后续新增元素，个数变化
				int dequeSize = linkedDeque.size();
				if (dequeSize > THREAD_NUM) {
					CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);
					for (int i = 0; i < THREAD_NUM; i++) {
						Message message = linkedDeque.poll();
						WorkTask workTask = new WorkTask(message, countDownLatch);
						new Thread(workTask).start();
					}
					try {
						countDownLatch.await();
						System.out.println("------------------------------Thread Size[" + THREAD_NUM + "] Run End------------------------------");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					CountDownLatch countDownLatch = new CountDownLatch(dequeSize);
					for (int i = 0; i < dequeSize; i++) {
						Message message = linkedDeque.poll();
						WorkTask workTask = new WorkTask(message, countDownLatch);
						new Thread(workTask).start();
					}
					try {
						countDownLatch.await();
						System.out.println("------------------------------Thread Size[" + dequeSize + "] Run End------------------------------");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
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
