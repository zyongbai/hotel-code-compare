package com.derby.entity;

import lombok.Data;
import com.derby.enums.EnvType;

@Data
public class Message implements Cloneable {
	private String hotelCode;
	private String roomTypeCode;
	private String ratePlanCode;
	private String account;
	private String checkInDate;
	private String checkOutDate;
	private String rooms;
	private String guests;
	private String totalBaseRate;
	private String totalOtherFees;
	private String totalTax;
	private EnvType envType;
	private String url;
	private String enableSmartcache;
	private String disableSmartengine;
	private String dailyBaseRate;
	private String rateType;
	
	/**
	 * 克隆
	 * 1、实现Cloneable接口
	 * 2、重写Object.clone()方法
	 * 
	 * @return
	 */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String toString() {
		StringBuilder description = new StringBuilder();
		
		description.append("Message[");
		description.append("hotelCode:").append(hotelCode).append(";");
		description.append("roomTypeCode:").append(roomTypeCode).append(";");
		description.append("ratePlanCode:").append(ratePlanCode).append(";");
		description.append("account:").append(account).append(";");
		description.append("checkInDate:").append(checkInDate).append(";");
		description.append("checkOutDate:").append(checkOutDate).append(";");
		description.append("rooms:").append(rooms).append(";");
		description.append("guests:").append(guests).append(";");
		description.append("totalBaseRate:").append(totalBaseRate).append(";");
		description.append("totalOtherFees:").append(totalOtherFees).append(";");
		description.append("totalTax:").append(totalTax).append(";");
		description.append("envType:").append(envType).append(";");
		description.append("url:").append(url);
		description.append("]");
		
		return description.toString();
	}
	
}
