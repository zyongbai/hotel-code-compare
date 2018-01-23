package com.derby.util;

import java.util.Map;
import java.util.List;
import org.dom4j.Element;
import java.util.HashMap;
import java.util.Iterator;
import org.dom4j.Document;
import java.util.ArrayList;
import java.io.InputStream;
import org.dom4j.io.SAXReader;
import com.derby.entity.Message;
import org.dom4j.DocumentException;

/**
 * ����XML����
 * 
 * @author ZHANGYONG415
 *
 */
public class Dom4jXml {
	/**
	 * ��InputStream����XML����ת����Message
	 * @return
	 */
	public static Map<String, List<Message>> xml2Message(Message ParamMessage, InputStream inputStream) {
		Map<String, List<Message>> resultMap = new HashMap<String, List<Message>>();
		List<Message> list = new ArrayList<Message>();
		
		// ����SAXReader����
		SAXReader reader = new SAXReader();
		try {
			// ��ȡ�ļ�,ת����Document
			Document document = reader.read(inputStream);
			
			Element root = document.getRootElement();
			// ��ȡ�ڵ�HotelRateList���������
			String checkIndate = root.attributeValue("CheckInDate");
			String checkOutDate = root.attributeValue("CheckOutDate");
			String rooms = root.attributeValue("Rooms");
			String guests = root.attributeValue("Guests");
			
			// ��ȡ�ڵ�HotelRate
			Element hotelRateElement = root.element("HotelRate");
			
			if (hotelRateElement != null) {
				String hotelCode = hotelRateElement.attributeValue("HotelCode");
				// �����ڵ�RoomRate
				Iterator<Element> iterator = hotelRateElement.elementIterator("RoomRate");
				while (iterator.hasNext()) {
					Element roomRateElement = iterator.next();
					// ��ȡ�ڵ�RoomRate���������
					String roomTypeCode = roomRateElement.attributeValue("RoomTypeCode");
					String ratePlanCode = roomRateElement.attributeValue("RatePlanCode");
					String totalBaseRate = roomRateElement.attributeValue("TotalBaseRate");
					String totalOtherFees = roomRateElement.attributeValue("TotalOtherFees");
					String totalTax = roomRateElement.attributeValue("TotalTax");
					String dailyBaseRate = roomRateElement.attributeValue("DailyBaseRate");
					String rateType = roomRateElement.attributeValue("RateType");
					
					Message message = new Message();
					message.setHotelCode(hotelCode);
					message.setAccount(ParamMessage.getAccount());
					message.setEnvType(ParamMessage.getEnvType());
					message.setCheckInDate(checkIndate);
					message.setCheckOutDate(checkOutDate);
					message.setRooms(rooms);
					message.setGuests(guests);
					message.setRoomTypeCode(roomTypeCode);
					message.setRatePlanCode(ratePlanCode);
					message.setTotalBaseRate(totalBaseRate);
					message.setTotalOtherFees(totalOtherFees);
					message.setTotalTax(totalTax);
					message.setDailyBaseRate(dailyBaseRate);
					message.setRateType(rateType);
					message.setUrl(ParamMessage.getUrl());
					list.add(message);
				}
				resultMap.put(hotelCode, list);
			} else {
				System.out.println("HotelCode:[" + ParamMessage.getHotelCode() + "]; HotelRate is null");
				
				list.add(ParamMessage);
				resultMap.put(ParamMessage.getHotelCode(), list);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			
			System.out.println("HotelCode:[" + ParamMessage.getHotelCode() + "]; HotelRate is null");
			
			list.add(ParamMessage);
			resultMap.put(ParamMessage.getHotelCode(), list);
		}
		
		return resultMap;
	}
	
}
