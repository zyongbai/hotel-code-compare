package com.derby.handler;

import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.sql.Connection;
import java.sql.SQLException;
import com.derby.util.DBUtil;
import com.derby.enums.EnvType;
import com.derby.entity.Message;
import java.sql.PreparedStatement;

public class DBHandler {
	public void insertData(Map<String, List<Message>> map) {
		Connection conn = DBUtil.getConnection();
		String sql = "";
		PreparedStatement ptst = null;
		
		for (Entry<String, List<Message>> entry : map.entrySet()) {
			List<Message> messageList = entry.getValue();
			for (Message message : messageList) {
				if (message.getEnvType() == EnvType.TEST) {
					sql = "insert into test_result(hotelCode,roomTypeCode,ratePlanCode,account,checkInDate,checkOutDate,rooms,guests,totalBaseRate,totalOtherFees,totalTax,url) values(?,?,?,?,?,?,?,?,?,?,?,?)";
				} else if (message.getEnvType() == EnvType.ONLINE) {
					sql = "insert into online_result(hotelCode,roomTypeCode,ratePlanCode,account,checkInDate,checkOutDate,rooms,guests,totalBaseRate,totalOtherFees,totalTax,url) values(?,?,?,?,?,?,?,?,?,?,?,?)";
				} else {
					continue;
				}
				
				try {
					ptst = conn.prepareStatement(sql);
					ptst.setString(1, message.getHotelCode() != null ? message.getHotelCode() : "");
					ptst.setString(2, message.getRoomTypeCode() != null ? message.getRoomTypeCode() : "");
					ptst.setString(3, message.getRatePlanCode() != null ? message.getRatePlanCode() : "");
					ptst.setString(4, message.getAccount() != null ? message.getAccount() : "");
					ptst.setString(5, message.getCheckInDate() != null ? message.getCheckInDate() : "");
					ptst.setString(6, message.getCheckOutDate() != null ? message.getCheckOutDate() : "");
					ptst.setString(7, message.getRooms() != null ? message.getRooms() : "");
					ptst.setString(8, message.getGuests() != null ? message.getGuests() : "");
					ptst.setString(9, message.getTotalBaseRate() != null ? message.getTotalBaseRate() : "");
					ptst.setString(10, message.getTotalOtherFees() != null ? message.getTotalOtherFees() : "");
					ptst.setString(11, message.getTotalTax() != null ? message.getTotalTax() : "");
					ptst.setString(12, message.getUrl() != null ? message.getUrl() : "");
					
					ptst.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					try {
						if (ptst != null) {
							ptst.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
