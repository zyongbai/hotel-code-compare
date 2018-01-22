package com.derby.concurrent.forkjoin;

import java.util.Map;
import java.util.List;
import com.derby.entity.Message;
import com.derby.concurrent.WorkQueue;
import com.derby.handler.MessageHandler;
import java.util.concurrent.RecursiveAction;

/**
 * 基于ForkJoin框架
 * 
 * @author ZHANGYONG415
 *
 */
public class MessageForkJoinTask extends RecursiveAction {
	private static final long serialVersionUID = 1L;

	private static final int THRESHOLD = 100;
	
	private WorkQueue workQueue = WorkQueue.getInstance();
	private List<String> hotelCodeList;
	private int start;
	private int end;
	
	public MessageForkJoinTask(List<String> hotelCodeList, int start, int end) {
		this.hotelCodeList = hotelCodeList;
		this.start = start;
		this.end = end;
	}
	
	@Override
	protected void compute() {
		if (end - start <= THRESHOLD) {
			for (int i = start; i < end; i++) {
				String hotelCode = hotelCodeList.get(i);
				
				Message message = new Message();
				message.setHotelCode(hotelCode);
				
				MessageHandler messageHandler = new MessageHandler();
				Map<String, Message> messageMap = messageHandler.get2Message(message);
				
				// 测试环境请求相关信息
				Message testMessage = messageMap.get("test");
				// 线上环境请求相关信息
				Message onlineMessage = messageMap.get("online");
				
				workQueue.offer(testMessage);
				workQueue.offer(onlineMessage);
			}
		} else {
			int mid = (start + end) / 2;
			MessageForkJoinTask leftTask = new MessageForkJoinTask(hotelCodeList, start, mid);
			MessageForkJoinTask rightTask = new MessageForkJoinTask(hotelCodeList, mid, end);
			invokeAll(leftTask, rightTask);
		}
	}

}
