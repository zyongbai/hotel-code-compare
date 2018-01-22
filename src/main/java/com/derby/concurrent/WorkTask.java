package com.derby.concurrent;

import java.util.Map;
import java.util.List;
import com.derby.enums.EnvType;
import com.derby.entity.Message;
import com.derby.entity.ResultMessage;
import java.util.concurrent.CountDownLatch;
import com.derby.handler.HttpRequestHandler;

/**
 * 工作线程,请求接口地址,返回结果,放入队列
 * 
 * @author ZHANGYONG415
 *
 */
public class WorkTask implements Runnable {
	private static ResultMessage resultMessageInstance = ResultMessage.getInstance();
	
	private Message message;
	private CountDownLatch countDownLatch;
	
	public WorkTask(Message message, CountDownLatch countDownLatch) {
		this.message = message;
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread() + "&Message:" + this.message);
		
		HttpRequestHandler handler = new HttpRequestHandler();
		Map<String, List<Message>> resultMap = handler.getRequest(message);
		
		if (message.getEnvType() == EnvType.TEST) {
			resultMessageInstance.addResult2Test(resultMap);
		} else if (message.getEnvType() == EnvType.ONLINE) {
			resultMessageInstance.addResult2Online(resultMap);
		} else {
			System.out.println("消息异常:没有找到对应的环境类型");
		}
		
		// 数据入库
//		DBQueue.getInstance().offer(resultMap);
		
		countDownLatch.countDown();
	}

}
