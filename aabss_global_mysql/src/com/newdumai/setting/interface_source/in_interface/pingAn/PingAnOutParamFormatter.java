package com.newdumai.setting.interface_source.in_interface.pingAn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


public class PingAnOutParamFormatter{
	
	private static final String JSON_RESULT = "result";
	private static final String JSON_DATA = "data";
	public static final int API_PHONE_INFO = 1;
	public static final int API_LOAN_CLASSIFY = 2;
	public static final int API_PHONE_TAG = 3;
	public static final int API_BLACK_LIST = 4;
	public static final int API_lOG_STATISTICS = 5;
	public static final int API_ID_CHECK = 6;
	public static final int API_PHONE_MATCH = 7;
	public static final int API_OVERDUE_CLASSIFY = 8;

	public static final String RULE_PHONE_INFO_GRAYSCALE = "grayscale";
	public static final String RULE_BLACK_LIST = "blacklist";
	public static final String RULE_OVER_DUE_CLASSIFY = "overdueClassify";

	public static final String API_TYPE_PHONE_TAGS = "tags";
	public static final String API_TYPE_PHONE_DIRECT_CALL = "directCall";
	public static final String API_TYPE_PHONE_COMMON_CONTACTS = "commonContacts";
	public static final String API_TYPE_PHONE_INTER_COMMONS = "interCommons";
	public static final String API_TYPE_PHONE_GRAY_SCALE = "grayscale";

	public static final String DATE_SCOPE_M0 = "M0";
	public static final String DATE_SCOPE_M1 = "M1";
	public static final String DATE_SCOPE_M2 = "M2";
	public static final String DATE_SCOPE_M3 = "M3";
	public static final String DATE_SCOPE_M4 = "M4";
	public static final String DATE_SCOPE_M5 = "M5";
	public static final String DATE_SCOPE_M6 = "M6";
	public static final String DATE_SCOPE_M9 = "M9";
	public static final String DATE_SCOPE_M12 = "M12";
	public static final String DATE_SCOPE_M24 = "M24";

	public static final String ORG_TYPE_BANK_CREDIT = "bankCredit";
	public static final String ORG_TYPE_BANK_LOAN = "bankLoan";
	public static final String ORG_TYPE_OTHER_CREDIT = "otherCredit";
	public static final String ORG_TYPE_OTHER_LOAN = "otherLoan";

	public static final String ORG_TYPE_NBFI = "NBFI";
	public static final String ORG_TYPE_BANK = "bank";
	public static final String ORG_TYPE_COLLECTION_AGENCY = "collectionAgency";

	public static final Integer DATA_RESULT_HIT = 0;
	public static final String DATA_RESULT_NO_HIT = "2";
	
	/**
	 * 联系人接口数据
	 * @param data
	 * {"idCard":"511081198011103036","name":"汤情","contactMain":"13708037508","contactPhone1":"18782096128","contactPhone2":"13730640583"}
	 * @return
	 * @throws Exception
	 * 
	 */
	public static String getContacts(Map<String,Object>params) throws Exception{
		String data = (String)params.get("result");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("contactTimes", getContactTimes(data));
		result.put("orgNums", getOrgNums(data));
		result.put("latestTime", getLatestTime(data));
		return new Gson().toJson(result);
	}
	
	/**
	 * 黑名单数据
	 * @param data
	 * 陈荣  --身份：511322199712265565手机：18806011366银行：6236691870000730594
	 * @return
	 * @throws Exception
	 */
	public static String getBlackList(Map<String,Object>params) throws Exception{
		String data = (String)params.get("result");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("orgLostContact", getOrgLostContact(data));
		result.put("bankLostContact", getBankLostContact(data));
		result.put("orgBlackList", getOrgBlackList(data));
		return new Gson().toJson(result);
	}
	
	/**
	 * 逾期接口数据
	 * @param data
	 * {"idCard":"612324198203212070","name":"李克勇","phone":"15208206210"}
	 * @return
	 * @throws Exception
	 */
	public static String getOverdue(Map<String,Object>params) throws Exception{
		String data = (String)params.get("result");
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("overdueOrgNumbs", getOverdueOrgNumbs(data));
		result.put("longestDays", getLongestDays(data));
		result.put("maxAmount",getMaxAmount(data));
		return new Gson().toJson(result);
	}
	
	
	private static String getLatestTime(String resultData) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode grayscale = data.get(API_TYPE_PHONE_GRAY_SCALE);
			Iterator<String> fields = grayscale.fieldNames();
			while (fields.hasNext()) {
				JsonNode mx = grayscale.get(fields.next());

				Iterator<String> mxfields = mx.fieldNames();
				while (mxfields.hasNext()) {
					String mxfield = mxfields.next();
					JsonNode content = mx.get(mxfield);
					JsonNode orgNode = null;
					if (content.get(ORG_TYPE_NBFI) != null && !content.get(ORG_TYPE_NBFI).asText().equals("null")) {
						orgNode = content.get(ORG_TYPE_NBFI);
					}

					if (content.get(ORG_TYPE_BANK) != null && !content.get(ORG_TYPE_BANK).asText().equals("null")) {
						orgNode = content.get(ORG_TYPE_BANK);
					}

					if (content.get(ORG_TYPE_COLLECTION_AGENCY) != null && !content.get(ORG_TYPE_COLLECTION_AGENCY).asText().equals("null") ) {
						orgNode = content.get(ORG_TYPE_COLLECTION_AGENCY);
					}
					if(mxfield.equals("M1") && orgNode!=null && isEmpty(orgNode.get("latestTime").asText())) {
						return "有";
					}
				}
			}
			
		}
		return "无";
	}
	private static int getOrgNums(String resultData)throws Exception {
		int contactTimes = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode grayscale = data
					.get(API_TYPE_PHONE_GRAY_SCALE);
			Iterator<String> fields = grayscale.fieldNames();
			while (fields.hasNext()) {
				JsonNode mx = grayscale.get(fields.next());

				Iterator<String> mxfields = mx.fieldNames();
				while (mxfields.hasNext()) {
					String mxfield = mxfields.next();
					JsonNode content = mx.get(mxfield);
					
					if (content.get(ORG_TYPE_NBFI) != null && !content.get(ORG_TYPE_NBFI).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_NBFI);
						contactTimes += orgNode.get("orgNumbs").asInt();
					}

					if (content.get(ORG_TYPE_BANK) != null && !content.get(ORG_TYPE_BANK).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_BANK);
						contactTimes += orgNode.get("orgNumbs").asInt();
					}

					if (content.get(ORG_TYPE_COLLECTION_AGENCY) != null && !content.get(ORG_TYPE_COLLECTION_AGENCY).asText().equals("null") ) {
						JsonNode orgNode = content.get(ORG_TYPE_COLLECTION_AGENCY);
						contactTimes += orgNode.get("orgNumbs").asInt();
					}
				}
			}
			
		}
		return contactTimes;
	}
	
	private static int getContactTimes(String resultData)throws Exception {
		int contactTimes = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode grayscale = data
					.get(API_TYPE_PHONE_GRAY_SCALE);
			Iterator<String> fields = grayscale.fieldNames();
			while (fields.hasNext()) {
				JsonNode mx = grayscale.get(fields.next());

				Iterator<String> mxfields = mx.fieldNames();
				while (mxfields.hasNext()) {
					String mxfield = mxfields.next();
					JsonNode content = mx.get(mxfield);
					
					if (content.get(ORG_TYPE_NBFI) != null && !content.get(ORG_TYPE_NBFI).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_NBFI);
						contactTimes += orgNode.get("contactTimes").asInt();
					}

					if (content.get(ORG_TYPE_BANK) != null && !content.get(ORG_TYPE_BANK).asText().equals("null")) {
						JsonNode orgNode = content.get(ORG_TYPE_BANK);
						contactTimes += orgNode.get("contactTimes").asInt();
					}

					if (content.get(ORG_TYPE_COLLECTION_AGENCY) != null && !content.get(ORG_TYPE_COLLECTION_AGENCY).asText().equals("null") ) {
						JsonNode orgNode = content.get(ORG_TYPE_COLLECTION_AGENCY);
						contactTimes += orgNode.get("contactTimes").asInt();
					}
				}
			}
			
		}
		return contactTimes;
	}

	private static String getOrgLostContact (String resultData)throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asText())) {
			JsonNode data = root.get(JSON_DATA);
			Iterator<JsonNode> others = data.get("others").elements();
			while (others.hasNext()) {
				JsonNode other = others.next();
				if(!isEmpty(other.get("orgLostContact").asText())){
					return "有";
				}
			}
		}
		return "无";
	}
	
	
	private static String getOrgBlackList(String resultData ) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asText())) {
			JsonNode data = root.get(JSON_DATA);
			Iterator<JsonNode> others = data.get("others").elements();
			while (others.hasNext()) {
				JsonNode other = others.next();
				if(!isEmpty(other.get("orgBlackList").asText())){
					return "有";
				}
			}
		}
		return "无";
	}
	private static String getBankLostContact(String resultData ) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(resultData);
		JsonNode dataResult = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(dataResult.asText())) {
			JsonNode data = root.get(JSON_DATA);
			Iterator<JsonNode> others = data.get("others").elements();
			while (others.hasNext()) {
				JsonNode other = others.next();
				if(!isEmpty(other.get("bankLostContact").asText())){
					return "有";
				}
			}
		}
		return "无";
	}
	private static int getOverdueOrgNumbs(String dataResult) throws Exception{
		int numbers = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			while (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				while (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);
						JsonNode node = null;
						if (mxContent.get(ORG_TYPE_BANK_CREDIT) != null && !mxContent.get(ORG_TYPE_BANK_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
						}
						if (mxContent.get(ORG_TYPE_OTHER_CREDIT) != null && !mxContent.get(ORG_TYPE_OTHER_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_OTHER_LOAN) != null && !mxContent.get(ORG_TYPE_OTHER_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_LOAN);
						}
						if(node!=null){
							numbers += node.get("orgNums").asInt();
						}
					}
				}
			}
		}
		return numbers;
	}
	private static int getLongestDays(String dataResult) throws Exception{
		int numbers = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			while (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				while (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);

						JsonNode node = null;
						if (mxContent.get(ORG_TYPE_BANK_CREDIT) != null && !mxContent.get(ORG_TYPE_BANK_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
						}
						if (mxContent.get(ORG_TYPE_OTHER_CREDIT) != null && !mxContent.get(ORG_TYPE_OTHER_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_OTHER_LOAN) != null && !mxContent.get(ORG_TYPE_OTHER_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_LOAN);
						}
						if(node!=null){
							numbers += node.get("longestDays").asInt();
						}
					}
				}
			}
		}
		return numbers;
	}
	private static int getMaxAmount(String dataResult) throws Exception{
		int maxAmount = 0;
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode root = objectMapper.readTree(dataResult);
		JsonNode result = root.get(JSON_RESULT);
		if (DATA_RESULT_HIT.equals(result.asInt())) {
			JsonNode data = root.get(JSON_DATA);
			JsonNode record = data.get("record");
			Iterator<JsonNode> classifications = record.elements();
			while (classifications.hasNext()) {
				JsonNode classification = classifications.next();
				Iterator<JsonNode> mx = classification.get("classification").elements();// 返回是一个json数组
				while (mx.hasNext()) {
					JsonNode mxJsonNode = mx.next();
					Iterator<String> mxfields = mxJsonNode.fieldNames();
					while (mxfields.hasNext()) {
						String mxfield = mxfields.next();
						JsonNode mxContent = mxJsonNode.get(mxfield);

						JsonNode node = null;
						if (mxContent.get(ORG_TYPE_BANK_CREDIT) != null && !mxContent.get(ORG_TYPE_BANK_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_BANK_LOAN) != null && !mxContent.get(ORG_TYPE_BANK_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_BANK_LOAN);
						}
						if (mxContent.get(ORG_TYPE_OTHER_CREDIT) != null && !mxContent.get(ORG_TYPE_OTHER_CREDIT).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_CREDIT);
						}
						if (mxContent.get(ORG_TYPE_OTHER_LOAN) != null && !mxContent.get(ORG_TYPE_OTHER_LOAN).asText().equals("null")) {
							node = mxContent.get(ORG_TYPE_OTHER_LOAN);
						}
						if(node!=null){
							String maxAmountScope = node.get("maxAmount").asText();
							String maxAmountString = maxAmountScope.substring(1,maxAmountScope.indexOf(","));
							maxAmount += Integer.parseInt(maxAmountString);
						}
					}
				}
			}
		}
		return maxAmount;
	}
	private static boolean isEmpty(String str){
		
		if(str == null){
			return true;
		}else if(str.trim().equals("") || str.trim().equals("null")){
			return true;
		}else{
			return false;
		}
	}
	
}
