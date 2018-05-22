package com.newdumai.jinjian.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.jinjian.InputOrderService;
import com.newdumai.util.DaoHelper;
import com.newdumai.util.JsonToMap;
import com.newdumai.util.MapObjUtil;

@Service("inputOrderService")
public class InputOrderServiceImpl extends BaseServiceImpl implements InputOrderService {
	
	@Autowired
	Dm_3rd_interfaceService dm_3rd_interfaceService;

	//xs_个人电子信息图像查询
	private static final String xiao_shi_photo_dm_3rd_interfaceCode = "93a69a72-d867-4f7d-a587-64ab3b8a3378";
	//优分肖像核查
	private static final String you_fen_photo_dm_3rd_interfaceCode = "4d0cbbed-3e68-414d-89d1-16d29ca43004";
	
	@Override
	public void addCase(Map<String, Object> person, Map<String, Object> order) {
		order = MapObjUtil.trimValueByKey(order, new String[]{"name","card_num","bank_num","mobile","plate","linkphone1","linkphone2"});
		person = MapObjUtil.trimValueByKey(person, new String[]{"name","card_num","banknum","mobile"});
		//鉴权成功，增加订单存储照片字段
		order.put("card_photo", getPhoto(order));
		String orderid = (String) order.get("orderid");
		if(isRepeatOrder(orderid)){
			//更新order表
			Map<String,Object>orderWhere = new HashMap<String,Object>();
			orderWhere.put("orderid", orderid);
			Update(order,"fk_orderinfo", orderWhere);
			//更新person表
			Map<String, Object> fk_orderinfoMap = mysqlSpringJdbcBaseDao.queryForMap(" select * from fk_orderinfo where orderid = '"+orderid+"' order by createtime desc limit 1 ");
			String personinfo_code = (String) fk_orderinfoMap.get("personinfo_code");
			Map<String,Object>personWhere = new HashMap<String,Object>();
			personWhere.put("code", personinfo_code);
			Update(person,"fk_personinfo", personWhere);
			//清空guize_detail表
			String order_code = (String) fk_orderinfoMap.get("code");
			mysqlSpringJdbcBaseDao.executeSql("DELETE from fk_guize_detail where order_code = '"+order_code+"'");
			//添加在order中加入repeat标签
			order.clear();
			order.putAll(fk_orderinfoMap);
			order.put("repeat", "");
		}else{
			String code = addAndRet(person, "fk_personinfo");
			order.put("personinfo_code", code);
			add(order, "fk_orderinfo");
		}
	}

	@SuppressWarnings("unchecked")
	private String getPhoto(Map<String,Object>order){
		String card_photo = "";
		try {
			System.out.println("【获取优分照片】");
			Map<String,Object> inPara = getInPara(you_fen_photo_dm_3rd_interfaceCode,order);
			String result = dm_3rd_interfaceService.testDS(you_fen_photo_dm_3rd_interfaceCode,inPara);
			Map<String,Object>resultMap = JsonToMap.jsonToMap(result);
			Map<String,Object>data = (Map<String, Object>) resultMap.get("data");
		    card_photo = (String)data.get("photo");
		} catch (Exception e) { 
			System.out.println("获取优分照片失败");
		}
		if(StringUtils.isEmpty(card_photo)){
			try {
				System.out.println("【获取小视照片】");
				Map<String,Object> inPara = getInPara(xiao_shi_photo_dm_3rd_interfaceCode, order);
				String result = dm_3rd_interfaceService.testDS(xiao_shi_photo_dm_3rd_interfaceCode, inPara);
				Map<String,Object>resultMap = JsonToMap.jsonToMap(result);
			    card_photo = (String)resultMap.get("PHOTO");
			} catch (Exception e) { 
				System.out.println("获取小视照片失败");
			}
		}
		return card_photo;
	}
	
	/*Check repeat order  begin*/
	public static boolean isRepeatOrder(String orderid){
		if(StringUtils.isEmpty(orderid)){
			return false;
		}
		List<Map<String, Object>> queryForList = DaoHelper.getDumai_newBaseDao().queryForList("select * from fk_orderinfo where orderid = '"+orderid+"' ");
		if(CollectionUtils.isEmpty(queryForList)){
			return false;
		}
		return true;
	}
	/*Check repeat order  end*/
	
	/**
	 * @description 转换订单参数到第三方数据源参数的映射方法，订单参数名->第三方数据源参数（输入参数）名
	 * @param dm_3rd_interface_code
	 * @param orderMap
	 * @return
	 */
	private static Map<String, Object> getInPara(String dm_3rd_interface_code, Map<String, Object> orderMap) {
		Map<String, Object> returnMap = MapObjUtil.getMapSortedByKey();
		List<Map<String, Object>> nameTransList = DaoHelper.getDumai_sourceBaseDao().queryForList("SELECT * FROM dm_3rd_interface_para WHERE 1=1 and `type`= '0' AND dm_3rd_interface_code= ? " ,new Object[]{dm_3rd_interface_code});
		if(CollectionUtils.isEmpty(nameTransList)){
			return null;
		}
		for (Map<String, Object> nameMap : nameTransList) {
			String or_name = (String) nameMap.get("fk_orderinfo_name");
			String is_name = (String) nameMap.get("name");
			// 根据参数映射转换参数
			if (orderMap.containsKey(or_name)) {
				returnMap.put(is_name, orderMap.get(or_name));
			}
		}
		return returnMap;
	}

	
	@Override
	public void updateCaseStep(Map<String, Object> personParams,
			Map<String, Object> personWhere, Map<String, Object> orderParams,
			Map<String, Object> orderWhere) {
		if(personParams != null){
			Update(personParams, "fk_personinfo_step", personWhere);
		}
		if(orderParams != null){
			Update(orderParams, "fk_orderinfo_step", orderWhere);
		}
	}

	@Override
	public void addCaseStep(Map<String, Object> person, Map<String, Object> order) {
		String code = addAndRet(person, "fk_personinfo_step");
		order.put("personinfo_code", code);
		add(order, "fk_orderinfo_step");
	}

	@Override
	public Map<String, Object> getPersonStepByCode(String code) {
		return mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_personinfo_step WHERE code = ?", new Object[]{code});
	}

	@Override
	public Map<String, Object> getOrderStepByCode(String code) {
		return mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo_step WHERE code = ?", new Object[]{code});
	}

	@Override
	public Map<String, Object> getOrderByCode(String code) {
		return mysqlSpringJdbcBaseDao.queryForMap("SELECT * FROM fk_orderinfo WHERE code = ?", code);
	}

	@Override
	public List<Map<String,Object>> getTypesOfAccount(String account) {
		String sql = "select t.* from sys_company_type ct join sys_type t on ct.type_code = t.code JOIN company_order ON company_order.sub_entity_id = ct.sub_entity_id where ct.is_able = '1' and company_order.account = ? ";
		return super.mysqlSpringJdbcBaseDao.queryForList(sql, account);
	}

	@Override
	public boolean isTestCompanyUsedUp(String account){
		// select is_test from company_order where sub_entity_id=?
		//select count(*) from fk_orderinfo where sub_entity_id=?;
		Map<String,Object> company = mysqlSpringJdbcBaseDao.queryForMap("select * from company_order where account=?",account);
		if(company!=null){
			String isTest = (String) company.get("is_test");
			if("1".equals(isTest)){
				Map<String,Object> data = mysqlSpringJdbcBaseDao.queryForMap("select count(*) cnt from fk_orderinfo where sub_entity_id=?", company.get("sub_entity_id"));
				Long count = (Long)data.get("cnt");
				return count>=5;
			}
			else return false;
		}
		return true;
	}
	
	
	
}
