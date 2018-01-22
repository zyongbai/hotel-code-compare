package com.derby.entity;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * ������
 * 
 * @author ZHANGYONG415
 *
 */
public class ResultMessage {
	private static ResultMessage resultMessage;
	
	private Map<String, List<Message>> testResultMessage = new HashMap<String, List<Message>>();
	private Map<String, List<Message>> onlineResultMessage = new HashMap<String, List<Message>>();
	
	private ResultMessage() {}
	
	/**
	 * ����
	 * @return
	 */
	public static synchronized ResultMessage getInstance() {
		if (resultMessage == null) {
			resultMessage = new ResultMessage();
		}
		return resultMessage;
	}
	
	/**
	 * ��Ӳ��Ի�����������
	 * @param result
	 */
	public void addResult2Test(Map<String, List<Message>> result) {
		testResultMessage.putAll(result);
	}
	
	/**
	 * ��ȡ���Ի�����������
	 * @return
	 */
	public Map<String, List<Message>> getResultFromTest() {
		return testResultMessage;
	}
	
	/**
	 * ��ȡ���Ի�����������������
	 * @return
	 */
	public int getTestResultSize() {
		return testResultMessage.size();
	}
	
	/**
	 * ������ϻ�����������
	 * @param result
	 */
	public void addResult2Online(Map<String, List<Message>> result) {
		onlineResultMessage.putAll(result);
	}
	
	/**
	 * ��ȡ���ϻ�����������
	 * @return
	 */
	public Map<String, List<Message>> getResultFromOnline() {
		return onlineResultMessage;
	}
	
	/**
	 * ��ȡ���ϻ�����������������
	 * @return
	 */
	public int getOnlineResultSize() {
		return onlineResultMessage.size();
	}
	
}
