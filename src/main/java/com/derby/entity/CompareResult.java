package com.derby.entity;

import lombok.Data;

@Data
public class CompareResult {
	private String hotelCode;
	private String roomTypeCode;
	private String ratePlanCode;
	private String account;
	private String checkInDate;
	private String checkOutDate;
	private String guests;
	private String rooms;
	private String testURL;
	private String prodURL;
	private String testResult;
	private String detail;
	
	public String toString() {
		StringBuilder description = new StringBuilder();
		
		description.append("CompareResult[");
		description.append("hotelCode:").append(hotelCode).append(";");
		description.append("roomTypeCode:").append(roomTypeCode).append(";");
		description.append("ratePlanCode:").append(ratePlanCode).append(";");
		description.append("account:").append(account).append(";");
		description.append("checkInDate:").append(checkInDate).append(";");
		description.append("checkOutDate:").append(checkOutDate).append(";");
		description.append("guests:").append(guests).append(";");
		description.append("rooms:").append(rooms).append(";");
		description.append("testURL:").append(testURL).append(";");
		description.append("prodURL:").append(prodURL).append(";");
		description.append("testResult:").append(testResult).append(";");
		description.append("detail:").append(detail);
		description.append("]");
		
		return description.toString();
	}
	
}
