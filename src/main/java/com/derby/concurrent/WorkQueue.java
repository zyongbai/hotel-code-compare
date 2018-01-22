package com.derby.concurrent;

import com.derby.entity.Message;
import com.derby.util.SystemMessage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * ��������
 * 
 * @author ZHANGYONG415
 *
 */
public class WorkQueue {
	private static final Integer THREAD_NUM = Integer.parseInt(SystemMessage.getString("THREAD_NUM"));// �����߳���
	
	private static WorkQueue workQueue;
	private static ConcurrentLinkedDeque<Message> linkedDeque = new ConcurrentLinkedDeque<Message>();
	
	private WorkQueue() {
		// ����һ���߳�ִ��run����(����˫�˶���linkedDeque)
		new Thread(() -> run()).start();
	}
	
	public static synchronized WorkQueue getInstance() {
		if (workQueue == null) {
			workQueue = new WorkQueue();
		}
		return workQueue;
	}
	
	/**
	 * ����Ϣʵ����ӵ��̰߳�ȫ�ķ�������˫�˶���
	 * @param message
	 */
	public void offer(Message message) {
		linkedDeque.offer(message);
	}
	
	/**
	 * ��˫�˶����л�ȡ��Ϣʵ��ִ��
	 */
	public void run() {
		while (true) {
			while (linkedDeque.size() > 0) {
				// ��ȡ˫�˶�����Ԫ�ظ�������ֹ��������Ԫ�أ������仯
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
				// ����2��
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
