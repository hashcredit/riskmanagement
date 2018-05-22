package com.newdumai.interfaces.iqianbang.huahua.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.interfaces.iqianbang.huahua.OrderHandelFlag;
import com.newdumai.interfaces.iqianbang.huahua.RiskService;
import com.newdumai.jinjian.InputOrderService;
import com.newdumai.order.control.AuditAsynchronousThread;
import com.newdumai.util.IdCardUtils;
import com.newdumai.util.TimeHelper;

/**
 * 花花接口的Service
 */
@Service("riskService")
public class RiskServiceImpl extends BaseServiceImpl implements RiskService {
	
	//private static final Map<String,String> SourceSubEntityIdMap;
	private static final String subEntityId = "38b562a1-020c-4e3c-883b-95cbe680cce9";
	/*static{
		 SourceSubEntityIdMap = new HashMap<String, String>();
		 SourceSubEntityIdMap.put("huahua", "38b562a1-020c-4e3c-883b-95cbe680cce9");
		 //SourceSubEntityIdMap.put("aiqiangbangbangshou", "20036f88-bb84-11e6-a87b-d05099ac8a40");
	}*/
	
	@Autowired
	InputOrderService inputOrderService;
	@Autowired
	private AuditAsynchronousThread auditAsynchronousThread;
	
	@Autowired
	private Dm_3rd_interfaceService dm_3rd_interfaceService;
	
	/**
	 * 入口
	 */
	@Override
	public Map<String, Object> doService(String command, Map<String, Object> param) {
		if ("CheckOrder".equals(command)) {
			return CheckOrder(param);
		} else if ("CarOrder".equals(command)) {
			return CarOrder(param);
		} else if ("GetNewAccount".equals(command)) {
			return GetNewAccount(param);
		}else {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("return", 4001);
			result.put("message", "command参数有错误。");
			return result;
		}
	}
	
	/**
	 * 1.1风控订单审核接口
	 * @param param
	 * @return
	 */
	private Map<String, Object> CheckOrder(Map<String, Object> param) {
		Map<String, Object> returnMap = new HashMap<String,Object>();
		try {
			returnMap = executeJinJian(param);
		} catch (Exception e) {
			returnMap.put("return", 10009);
			returnMap.put("message", "解析订单信息出错" + e.getMessage());
		}
		return returnMap;
	}
	
	/**
	 * 1.3风控车贷订单审核接口
	 * @param param
	 * @return
	 */
	private Map<String, Object> CarOrder(Map<String, Object> param){
		Map<String, Object> returnMap = new HashMap<String,Object>();
		try {
			returnMap = executeJinJian(param);
		} catch (Exception e) {
			returnMap.put("return", 10009);
			returnMap.put("message", "解析订单信息出错" + e.getMessage());
		}
		return returnMap;
	}

	/**
	 * 1.8获取放款用户信息
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> GetNewAccount(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Map<String,Object> datainfoMain = null;
		
		datainfoMain =  (Map<String, Object>) param.get("data");
		String dataJson = (String) param.get("dataJson");
		String traceNo = (String) datainfoMain.get("traceNo");
		
		try {
			Map<String,Object> order = mysqlSpringJdbcBaseDao.queryForMap("select code from fk_orderInfo where trace_no=? and sub_entity_id=?", traceNo,subEntityId);
			mysqlSpringJdbcBaseDao.update("update fk_orderInfo set biz_range=?,loan_info=? where code=?", 2,dataJson,order.get("code"));
		} catch (Exception e) {
			e.printStackTrace();
			result.put("return", 4008);
			result.put("message", "接收花花等 放款数据 处理失败！");
			return result;
		}
		result.put("return", 4008);
		result.put("message", "接受放款用户信息成功！");
		return result;

	}

	private String getTypeCodeByNumCode(String num_code){
		String sql = "select * from sys_type where is_able = '1' and num_code = '"+num_code+"'";
		Map<String, Object> sys_typeMap = super.mysqlSpringJdbcBaseDao.queryForMap(sql);
		if(CollectionUtils.isEmpty(sys_typeMap)){
			return null;
		}else{
			return (String) sys_typeMap.get("code");
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,Object>executeJinJian(Map<String,Object> map){
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String,Object> datainfoMain = (Map<String, Object>) map.get("data");
		String jsonData =  (String) map.get("jsonData");
		if (CollectionUtils.isEmpty(datainfoMain)) {
			result.put("return", 4001);
			result.put("message", "发送的数据不能为空！");
			return result;
		}
		Map<String,Object> orderinfo = (Map<String, Object>) datainfoMain.get("orderInfo");
		Map<String,Object> checkinfo = (Map<String, Object>) datainfoMain.get("checkInfo");
		if(CollectionUtils.isEmpty(orderinfo)||CollectionUtils.isEmpty(checkinfo)){
			result.put("return", 10009);
			result.put("message", "orderInfo和checkInfo不能为空！");
			return result;
		}
		
		String name                = (String) checkinfo.get("realName"); // "真实姓名",
		String idcard              = (String) checkinfo.get("idCard"); // "身份证号码",
		String address             = (String) checkinfo.get("addProvince"); // "常住详址",
		String married             = (String) checkinfo.get("marriedStatus"); // "婚姻状况",
		String profession          = (String) checkinfo.get("job"); // "职业",
		String bankname            = (String) checkinfo.get("bankName"); // "常用银行名称",
		String banknum             = (String) checkinfo.get("bankNo"); // "常用银行卡号",
		String phone               = (String) checkinfo.get("phone"); // "手机号",
		String mobilePWD           = (String) checkinfo.get("serverPWD"); // "手机号6位服务密码",
		String education           = (String) checkinfo.get("education"); // "学历",
		String income              = (String) checkinfo.get("income"); // "月收入金额",
		String otherincome         = (String) checkinfo.get("otherincome"); // "其他月收入金额"
		String insuranceid         = (String) checkinfo.get("insuranceid"); // 社会保险账户
		String insurancepwd        = (String) checkinfo.get("insurancepwd"); // 社会保险密码
		String fundid              = (String) checkinfo.get("fundid"); // 公积金账号
		String fundpwd             = (String) checkinfo.get("fundpwd"); // 公积金密码
		//现金贷                   
		String linkname1           = (String) checkinfo.get("contactName1"); // "联系人1姓名",
		String linkphone1          = (String) checkinfo.get("contactPhone1"); // "联系人1电话", 
		String linksex1            = (String) checkinfo.get("contactSex1"); // "联系人1电话", 
		String linkReation1        = (String) checkinfo.get("contactRelation1"); // "联系人1关系", 
		String linkname2           = (String) checkinfo.get("contactName2"); // "联系人2姓名",
		String linkphone2          = (String) checkinfo.get("contactPhone2"); // "联系人2电话"
		String linksex2		       = (String) checkinfo.get("contactSex2"); // "联系人2电话", 
		String linkReation2        = (String) checkinfo.get("contactRelation2"); // "联系人2关系", 
		String linkname3           = (String) checkinfo.get("contactName3"); // "联系人3姓名",
		String linkphone3          = (String) checkinfo.get("contactPhone3"); // "联系人3电话"
		String linksex3		       = (String) checkinfo.get("contactSex3"); // "联系人3电话", 
		String linkReation3        = (String) checkinfo.get("contactRelation3"); // "联系人3关系", 
		String company             = (String) checkinfo.get("company"); // "联系人当前所在公司名称", 
		String company_address     = (String) checkinfo.get("companyAddress"); // "联系人当前所在公司名称", 
		String company_phone       = (String) checkinfo.get("companyPhone"); // "联系人当前所在公司名称", 
		String colleague_name1     = (String) checkinfo.get("colleagueName1"); // "联系人当前所在公司同事姓名1", 
		String colleague_phone1    = (String) checkinfo.get("colleaguePhone1"); // "联系人当前所在公司同事电话1", 
		String colleague_name2     = (String) checkinfo.get("colleagueName2"); // "联系人当前所在公司同事姓名2", 
		String colleague_phone2    = (String) checkinfo.get("colleaguePhone2"); // "联系人当前所在公司同事电话2", 
		String service_organization= (String) checkinfo.get("serviceOrganization"); // "借款人服务机构名称（商户名称）", 
		String sqje                = (String) orderinfo.get("amount"); // 借款金额
		String jkqx                = (String) orderinfo.get("instalmentTerms"); // 借款期限 月数
		String organization        = (String) orderinfo.get("organization");// 合作机构-医院
		String projectname         = (String) orderinfo.get("projectname"); // 美容项目
		String plate               = (String) orderinfo.get("plate"); // 
		String plateType           = (String) orderinfo.get("plateType"); // 
		String num_code            = (String) orderinfo.get("thetype");// 业务类型
		String laiyuan = (String) datainfoMain.get("source");
		String trace_no = (String) datainfoMain.get("traceNo");
		String noticeUrl = (String) datainfoMain.get("noticeUrl");
		Double projectprice        = (Double) orderinfo.get("projectprice");// 
		String thetype      = getTypeCodeByNumCode(num_code);
		if(StringUtils.isEmpty(thetype)){
			result.put("return", 10009);
			result.put("message", "thetype业务类型没有找到，或者已经停用，请联系我...");
			return result;
		}
		
		String orderId =(String) orderinfo.get("orderId");// 第三方交易订单编号
		if(StringUtils.isEmpty(orderId)){
			result.put("return", 10009);
			result.put("message", "orderId第三方交易订单编号不能为空！");
			return result;
		}
		
		String date = TimeHelper.getCurrentTime();
		
		Map<String, Object> person = new HashMap<String, Object>();
		person.put("createtime", date);
		// 1人员基本信息
		person.put("name", name);// Name
		person.put("card_num", idcard);// Card_num
		person.put("address", address);// Address
		person.put("Married", married);// Married
		person.put("profession", profession);// Profession
		person.put("bankname", bankname); // Bankname
		person.put("banknum", banknum); // Banknum
		person.put("mobile", phone);// Mobile
		person.put("mobilePWD", mobilePWD);// Mobilepwd
		person.put("education", education);//
		person.put("income",Integer.parseInt((income==null||"".equals(income))?"0":income));//
		person.put("otherincome", Integer.parseInt((otherincome==null||"".equals(otherincome))?"0":otherincome));//
		person.put("insuranceid", insuranceid);// 社会保险账户
		person.put("insurancepwd", insurancepwd);// 社会保险密码
		person.put("fundid", fundid);// 公积金账号
		person.put("fundpwd", fundpwd);// 公积金密码
		person.put("sub_entity_id", subEntityId);
		//现金贷
		person.put("linkname1", linkname1);// 联系人1
		person.put("linkphone1", linkphone1);// 联系人1手机号
		person.put("linksex1", linksex1);// 联系人1性别
		person.put("linkReation1", linkReation1);// 联系人1关系
		person.put("linkname2", linkname2);// 联系人2
		person.put("linkphone2", linkphone2);// 联系人2手机号
		person.put("linksex2", linksex2);// 联系人2性别
		person.put("linkReation2", linkReation2);// 联系人2关系
		person.put("linkname3", linkname3);// 联系人3
		person.put("linkphone3", linkphone3);// 联系人3手机号
		person.put("linksex3", linksex3);// 联系人3性别
		person.put("linkReation3", linkReation3);// 联系人2关系
		person.put("company",company);// "联系人当前所在公司名称", 
		person.put("company_address",company_address);// "联系人当前所在公司名称", 
		person.put("company_phone",company_phone);//  联系人当前所在公司名称", 
		person.put("colleague_name1",colleague_name1);// "联系人当前所在公司同事姓名1", 
		person.put("colleague_phone1",colleague_phone1);//  联系人当前所在公司同事电话1", 
		person.put("colleague_name2",colleague_name2);// "联系人当前所在公司同事姓名2", 
		person.put("colleague_phone2",colleague_phone2);//  联系人当前所在公司同事电话2", 
		person.put("service_organization",service_organization);// "借款人服务机构名称（商户名称）", 
		
		if (idcard != null && idcard.length() > 0) {
			String sex = IdCardUtils.getGenderByIdCard(idcard); // M-男，F-女，N-未知
			if ("M".equals(sex)) sex = "男";
			else if ("F".equals(sex)) sex = "女";
			else sex = "未知";
			person.put("sex", sex);
			person.put("age", IdCardUtils.getAgeByIdCard(idcard));// 年龄
			person.put("Birthplace",IdCardUtils.getProvinceByIdCard(idcard));// 籍贯，取自身份证的解析
		}
		
		// 2订单信息
		Map<String, Object> order = new HashMap<String, Object>();
		order.put("laiyuan",laiyuan);
		order.put("trace_no",trace_no);
		order.put("noticeUrl",noticeUrl);
		order.put("status1", "0");
		order.put("status2", "0");
		order.put("createtime", date);
		order.put("thetype", thetype);
		order.put("name", name);// Name
		order.put("card_num", idcard);// Card_num
		order.put("bank_num", banknum);// Card_num
		order.put("mobile", phone);// Mobile
		order.put("linkphone1", linkphone1);// linkphone1
		order.put("linkphone2", linkphone2);// linkphone2
		order.put("sqje", Float.parseFloat(sqje==null?"0":sqje));// 借款金额
		order.put("Jkqx", Integer.parseInt(jkqx==null?"0":jkqx));// 借款期限
		order.put("organization", organization);// 合作机构
		order.put("projectname", projectname);// 合作项目
		order.put("projectprice", projectprice);
		order.put("sub_entity_id", subEntityId);
		order.put("plate", plate);
		order.put("plateType", plateType);
		order.put("orderid", orderId);
		order.put("jsonData", jsonData);
		
		//测试单拦截
		String order_handel_flag = (String) datainfoMain.get("order_handel_flag");
		if(OrderHandelFlag.test_f.name().equals(order_handel_flag)||OrderHandelFlag.test_s.name().equals(order_handel_flag)){
			return OrderHandelFlag.goTestWay(order,order_handel_flag);
		}
		//测试单拦截暂未启用
//		else if(!OrderHandelFlag.pro.name().equals(order_handel_flag)){
//			result.put("return", 10009);
//			result.put("message","正式单请添加正式单标志！");
//			return result;
//		}
		Map<String,Object> validateResult = dm_3rd_interfaceService.validateInputOrderAll(order);
		if(Boolean.FALSE.equals(validateResult.get("success"))){ 
			result.put("return", 10009);
			result.put("message", validateResult.get("invalidMsg"));
			return result;
		}
		
		inputOrderService.addCase(person, order);
		auditAsynchronousThread.doAuditByOrder(order);
		result.put("return", 10008);
		result.put("message", "提交成功，等待审核");
		return result;
	}
	
}
