package com.derby.handler;

import java.io.File;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.io.BufferedReader;
import com.derby.enums.EnvType;
import com.derby.entity.Message;
import com.derby.util.SystemMessage;
import java.io.FileNotFoundException;
import java.time.temporal.ChronoUnit;
import com.derby.entity.CompareResult;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ��Ϣ������
 * @author ZHANGYONG415
 *
 */
public class MessageHandler {
	private static final String DATE_FORMAT = "yyyy-MM-dd";// ���ڸ�ʽ:��-��-��
	private static final int DATE_RANDOM = 7;// ��ǰ������������������ֵ
	private static final int DATE_PERIOD_RANDOM = 2;// ��סʱ�����뿪ʱ��,�������������ֵ
	private static final int ROOM_RANDOM = 2;// ������������ֵ
	private static final int PERSON_RANDOM = 2;// ÿ��������ס����������ֵ
	
	// ���Ի��������ַ
	private static String test_url = SystemMessage.getString("test_url");
	// ���ϻ��������ַ
	private static String online_url = SystemMessage.getString("online_url");;
	
	private ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
	
	/**
	 * ��ȡ���Ի��������ϻ���������������Ϣ
	 * @param message
	 * @return
	 */
	public Map<String, Message> get2Message(Message message) {
		Map<String, Message> resultMessage = new HashMap<String, Message>();
		
		message = getRandomMessage(message);
		
		// ���Ի������������Ϣ
		Message testMessage = (Message) message.clone();
		testMessage.setEnvType(EnvType.TEST);
		String testUrl = test_url.replace(":account", testMessage.getAccount()).replace(":checkInDate", testMessage.getCheckInDate())
								 .replace(":checkOutDate", testMessage.getCheckOutDate()).replace(":hotelCode", testMessage.getHotelCode())
								 .replace(":guests", testMessage.getGuests()).replace(":rooms", testMessage.getRooms());
		testMessage.setUrl(testUrl);
		System.out.println(testMessage.toString());
		
		// ���ϻ������������Ϣ
		Message onlineMessage = (Message) message.clone();
		onlineMessage.setEnvType(EnvType.ONLINE);
		String onlineUrl = online_url.replace(":account", onlineMessage.getAccount()).replace(":checkInDate", onlineMessage.getCheckInDate())
				 				   	 .replace(":checkOutDate", onlineMessage.getCheckOutDate()).replace(":hotelCode", onlineMessage.getHotelCode())
				 				   	 .replace(":guests", onlineMessage.getGuests()).replace(":rooms", onlineMessage.getRooms());
		onlineMessage.setUrl(onlineUrl);
		System.out.println(onlineMessage.toString());
		
		resultMessage.put("test", testMessage);
		resultMessage.put("online", onlineMessage);
		
		return resultMessage;
	}
	
	/**
	 * ������������Ϣ
	 * @param message
	 * @return
	 */
	public Message getRandomMessage(Message message) {
		
		// ���account
		String[] accountArray = {"marriott-roomkey", "marriott-google", "marriottz-google", "marriott-facebook", "marriott-criteo", "marriott-facebook-sale", 
								 "marriott-tripadvisor-instantbooking", "marriott-tts", "marriott-hipmunk", "marriott-hotelscan", "marriott-kayak", "marriott-kayakprefetch", 
								 "marriott-qunar", "marriott-skyscanner", "marriott-tripadvisor", "marriott-trivago", "marriott-wego", "tripadvisor", 
								 "qunar", "marriott-qa", "marriott-bing", "marriott-trivago-expressbooking"};
		// accountArray�����±������
		int accountRandom = threadLocalRandom.nextInt(accountArray.length);
		String account = accountArray[accountRandom];
		message.setAccount(account);
		
		LocalDate localDate = LocalDate.now();
		// ��ס���������(�ӵ�ǰʱ�俪ʼ����)
		int dateRandom = threadLocalRandom.nextInt(DATE_RANDOM);
		LocalDate startDate = localDate.plus(dateRandom, ChronoUnit.DAYS);
		String checkInDate = startDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		message.setCheckInDate(checkInDate);
		
		// ��סʱ�����뿪ʱ���������
		int datePeriodRandom = threadLocalRandom.nextInt(DATE_PERIOD_RANDOM) + 1;
		LocalDate endDate = startDate.plus(datePeriodRandom, ChronoUnit.DAYS);
		String checkOutDate = endDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		message.setCheckOutDate(checkOutDate);
		
		// ���������
		int rooms = threadLocalRandom.nextInt(ROOM_RANDOM) + 1;
		// ÿ�䷿�����������
		int persons = threadLocalRandom.nextInt(PERSON_RANDOM) + 1;
		int guests = rooms * persons;
		message.setRooms(String.valueOf(rooms));
		message.setGuests(String.valueOf(guests));
		
		return message;
	}
	
	/**
	 * �Ƚϲ��Ի��������ϻ�����������
	 * 
	 * @param testResultMessage
	 * @param onlineResultMessage
	 * @return
	 */
	public Map<String, List<CompareResult>> compareTestAndOnline(Map<String, List<Message>> testResultMessage, 
																 Map<String, List<Message>> onlineResultMessage) {
		Map<String, List<CompareResult>> compareResultMap = new HashMap<String, List<CompareResult>>();
		
		for (Entry<String, List<Message>> testEntry : testResultMessage.entrySet()) {
			String testHotelCode = testEntry.getKey();
			List<Message> testHotelCodeList = testEntry.getValue();
			
			// ���Ͻ�����ݴ��ڲ��Խ���е�HotelCode
			if (onlineResultMessage.containsKey(testHotelCode)) {
				List<Message> onlineHotelCodeList = onlineResultMessage.get(testHotelCode);
				
				if ((testHotelCodeList.size() == 1 && testHotelCodeList.get(0).getRoomTypeCode() == null) && 
						(onlineHotelCodeList.size() == 1 && onlineHotelCodeList.get(0).getRoomTypeCode() == null)) {
					// ���Ի��������ϻ������ݶ�Ϊ��,��Ϊ�ȽϽ����ͬ
					
				} else if ((testHotelCodeList.size() == 1 && testHotelCodeList.get(0).getRoomTypeCode() == null) && 
						((onlineHotelCodeList.size() == 1 && onlineHotelCodeList.get(0).getRoomTypeCode() != null) || (onlineHotelCodeList.size() > 1))) {
					// ���Ի���Ϊ��,���ϻ������ݲ�Ϊ��,�ȽϽ����ͬ
					Message firstMessage = onlineHotelCodeList.get(0);
					
					CompareResult compareResult = new CompareResult();
					compareResult.setHotelCode(firstMessage.getHotelCode());
					compareResult.setRoomTypeCode(firstMessage.getRoomTypeCode());
					compareResult.setRatePlanCode(firstMessage.getRatePlanCode());
					compareResult.setAccount(firstMessage.getAccount());
					compareResult.setCheckInDate(firstMessage.getCheckInDate());
					compareResult.setCheckOutDate(firstMessage.getCheckOutDate());
					compareResult.setGuests(firstMessage.getGuests());
					compareResult.setRooms(firstMessage.getRooms());
					compareResult.setTestURL(null);
					compareResult.setProdURL(firstMessage.getUrl());
					compareResult.setTestResult("���ݲ�һ��");
					String detail = "{test:null,online:" + onlineHotelCodeList.toString() + "}";
					detail = detail.replace(",", "��");
					compareResult.setDetail(detail);
					
					List<CompareResult> compareResultList = null;
					if (compareResultMap.containsKey(testHotelCode)) {
						compareResultList = compareResultMap.get(testHotelCode);
						if (compareResultList == null) {
							compareResultList = new ArrayList<CompareResult>();
						}
					} else {
						compareResultList = new ArrayList<CompareResult>();
					}
					
					compareResultList.add(compareResult);
					compareResultMap.put(testHotelCode, compareResultList);
				} else if ((onlineHotelCodeList.size() == 1 && onlineHotelCodeList.get(0).getRoomTypeCode() == null) && 
						((testHotelCodeList.size() == 1 && testHotelCodeList.get(0).getRoomTypeCode() != null) || (testHotelCodeList.size() > 1))) {
					// ���ϻ���Ϊ��,���Ի������ݲ�Ϊ��,�ȽϽ����ͬ
					Message firstMessage = testHotelCodeList.get(0);
					
					CompareResult compareResult = new CompareResult();
					compareResult.setHotelCode(firstMessage.getHotelCode());
					compareResult.setRoomTypeCode(firstMessage.getRoomTypeCode());
					compareResult.setRatePlanCode(firstMessage.getRatePlanCode());
					compareResult.setAccount(firstMessage.getAccount());
					compareResult.setCheckInDate(firstMessage.getCheckInDate());
					compareResult.setCheckOutDate(firstMessage.getCheckOutDate());
					compareResult.setGuests(firstMessage.getGuests());
					compareResult.setRooms(firstMessage.getRooms());
					compareResult.setTestURL(firstMessage.getUrl());
					compareResult.setProdURL(null);
					compareResult.setTestResult("���ݲ�һ��");
					String detail = "{test:" + testHotelCodeList.toString() + ",online:null}";
					detail = detail.replace(",", "��");
					compareResult.setDetail(detail);
					
					List<CompareResult> compareResultList = null;
					if (compareResultMap.containsKey(testHotelCode)) {
						compareResultList = compareResultMap.get(testHotelCode);
						if (compareResultList == null) {
							compareResultList = new ArrayList<CompareResult>();
						}
					} else {
						compareResultList = new ArrayList<CompareResult>();
					}
					
					compareResultList.add(compareResult);
					compareResultMap.put(testHotelCode, compareResultList);
				} else {
					for (int i = 0; i < testHotelCodeList.size(); i++) {
						Message testMessage = testHotelCodeList.get(i);
						for (int j = 0; j < onlineHotelCodeList.size(); j++) {
							Message onlineMessage = onlineHotelCodeList.get(j);
							if (testMessage.getRoomTypeCode().equals(onlineMessage.getRoomTypeCode()) && 
									testMessage.getRatePlanCode().equals(onlineMessage.getRatePlanCode())) {
								BigDecimal testTotalBaseRate = new BigDecimal(testMessage.getTotalBaseRate());
								BigDecimal testTotalOtherFees = new BigDecimal(testMessage.getTotalOtherFees());
								BigDecimal testTotalTax = new BigDecimal(testMessage.getTotalTax());
								
								BigDecimal onlineTotalBaseRate = new BigDecimal(onlineMessage.getTotalBaseRate());
								BigDecimal onlineTotalOtherFees = new BigDecimal(onlineMessage.getTotalOtherFees());
								BigDecimal onlineTotalTax = new BigDecimal(onlineMessage.getTotalTax());
								
								BigDecimal compareFlag1 = testTotalBaseRate.subtract(onlineTotalBaseRate);
								BigDecimal compareFlag2 = testTotalOtherFees.subtract(onlineTotalOtherFees);
								BigDecimal compareFlag3 = testTotalTax.subtract(onlineTotalTax);
								
								if ((compareFlag1.doubleValue() != 0.01 && compareFlag1.doubleValue() != -0.01 && compareFlag1.doubleValue() != 0) || 
										(compareFlag2.doubleValue() != 0.01 && compareFlag2.doubleValue() != -0.01 && compareFlag2.doubleValue() != 0) || 
										(compareFlag3.doubleValue() != 0.01 && compareFlag3.doubleValue() != -0.01 && compareFlag3.doubleValue() != 0)) {
									// ���ݲ�һ��
									CompareResult compareResult = new CompareResult();
									compareResult.setHotelCode(testMessage.getHotelCode());
									compareResult.setRoomTypeCode(testMessage.getRoomTypeCode());
									compareResult.setRatePlanCode(testMessage.getRatePlanCode());
									compareResult.setAccount(testMessage.getAccount());
									compareResult.setCheckInDate(testMessage.getCheckInDate());
									compareResult.setCheckOutDate(testMessage.getCheckOutDate());
									compareResult.setGuests(testMessage.getGuests());
									compareResult.setRooms(testMessage.getRooms());
									compareResult.setTestURL(testMessage.getUrl());
									compareResult.setProdURL(onlineMessage.getUrl());
									compareResult.setTestResult("���ݲ�һ��");
									String detail = "{test:" + testMessage.toString() + ",online:" + onlineMessage.toString() + "}";
									detail = detail.replace(",", "��");
									compareResult.setDetail(detail);
									
									List<CompareResult> compareResultList = null;
									if (compareResultMap.containsKey(testHotelCode)) {
										compareResultList = compareResultMap.get(testHotelCode);
										if (compareResultList == null) {
											compareResultList = new ArrayList<CompareResult>();
										}
									} else {
										compareResultList = new ArrayList<CompareResult>();
									}
									
									compareResultList.add(compareResult);
									compareResultMap.put(testHotelCode, compareResultList);
								}
								
								break;
							}
						}
					}
				}
			}
		}
		
		return compareResultMap;
	}
	
	/**
	 * ��csv�ļ��л�ȡhotelCode
	 * @return
	 */
	public List<String> getHotelCodeFromCSV() {
		List<String> list = new ArrayList<String>();
		
		String path = MessageHandler.class.getResource("/").getPath();
		File file = new File(path, "hotelCode/migrate_to_dswitch_hotel_info.csv");
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			br.readLine();
			String readStr = "";
			while ((readStr = br.readLine()) != null) {
				list.add("MAR-" + readStr.split(",")[2]);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
}
