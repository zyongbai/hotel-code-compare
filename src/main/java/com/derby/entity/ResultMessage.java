package com.derby.entity;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

/**
 * 请求结果
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
	 * 单例
	 * @return
	 */
	public static synchronized ResultMessage getInstance() {
		if (resultMessage == null) {
			resultMessage = new ResultMessage();
		}
		return resultMessage;
	}
	
	/**
	 * 添加测试环境的请求结果
	 * @param result
	 */
	public void addResult2Test(Map<String, List<Message>> result) {
		testResultMessage.putAll(result);
	}
	
	/**
	 * 获取测试环境的请求结果
	 * @return
	 */
	public Map<String, List<Message>> getResultFromTest() {
		return testResultMessage;
	}
	
	/**
	 * 获取测试环境的请求结果的数量
	 * @return
	 */
	public int getTestResultSize() {
		return testResultMessage.size();
	}
	
	/**
	 * 添加线上环境的请求结果
	 * @param result
	 */
	public void addResult2Online(Map<String, List<Message>> result) {
		onlineResultMessage.putAll(result);
	}
	
	/**
	 * 获取线上环境的请求结果
	 * @return
	 */
	public Map<String, List<Message>> getResultFromOnline() {
		return onlineResultMessage;
	}
	
	/**
	 * 获取线上环境的请求结果的数量
	 * @return
	 */
	public int getOnlineResultSize() {
		return onlineResultMessage.size();
	}
	
}
