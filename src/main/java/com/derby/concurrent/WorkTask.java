package com.derby.concurrent;

import java.util.Map;
import java.util.List;
import com.derby.enums.EnvType;
import com.derby.entity.Message;
import com.derby.entity.ResultMessage;
import java.util.concurrent.CountDownLatch;
import com.derby.handler.HttpRequestHandler;

/**
 * �����߳�,����ӿڵ�ַ,���ؽ��,�������
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
			System.out.println("��Ϣ�쳣:û���ҵ���Ӧ�Ļ�������");
		}
		
		// �������
//		DBQueue.getInstance().offer(resultMap);
		
		countDownLatch.countDown();
	}

}
