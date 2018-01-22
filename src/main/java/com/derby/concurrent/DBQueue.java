package com.derby.concurrent;

import java.util.Map;
import java.util.List;
import com.derby.entity.Message;
import com.derby.handler.DBHandler;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * ����������
 * 
 * @author ZHANGYONG415
 *
 */
public class DBQueue {
	private static DBQueue dbQueue;
	private static ConcurrentLinkedDeque<Map<String, List<Message>>> linkedDeque = new ConcurrentLinkedDeque<Map<String, List<Message>>>();
	
	private DBQueue() {
		// ����һ���߳�ִ��run����(����˫�˶���linkedDeque)
		new Thread(() -> run()).start();
	}
	
	public static synchronized DBQueue getInstance() {
		if (dbQueue == null) {
			dbQueue = new DBQueue();
		}
		return dbQueue;
	}
	
	/**
	 * ����Ϣʵ����ӵ��̰߳�ȫ�ķ�������˫�˶���
	 * @param map
	 */
	public void offer(Map<String, List<Message>> map) {
		linkedDeque.offer(map);
	}
	
	/**
	 * ��˫�˶����л�ȡ��Ϣʵ��ִ��
	 */
	public void run() {
		while (true) {
			while (linkedDeque.size() > 0) {
				Map<String, List<Message>> map = linkedDeque.poll();
				DBHandler dbHandler = new DBHandler();
				dbHandler.insertData(map);
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
