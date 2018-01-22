package com.derby.handler;

import java.util.Map;
import java.util.List;
import java.sql.Connection;
import java.util.Map.Entry;
import com.derby.util.DBUtil;
import com.derby.enums.EnvType;
import com.derby.entity.Message;

public class DBHandler {
	public void insertData(Map<String, List<Message>> map) {
		Connection conn = DBUtil.getConnection();
		String sql = "";
		
		for (Entry<String, List<Message>> entry : map.entrySet()) {
			String hotelCode = entry.getKey();
			List<Message> messageList = entry.getValue();
			for (Message message : messageList) {
				if (message.getEnvType() == EnvType.TEST) {
					sql = "";
				} else if (message.getEnvType() == EnvType.ONLINE) {
					sql = "";
				} else {
					continue;
				}
			}
		}
	}
}
