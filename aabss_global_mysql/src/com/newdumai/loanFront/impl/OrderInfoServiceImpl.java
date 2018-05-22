package com.newdumai.loanFront.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.loanFront.OrderInfoService;


@Service("orderInfoService")
public class OrderInfoServiceImpl extends BaseServiceImpl implements OrderInfoService{

	@Override
	public Map<String, Object> findByCode(String code) {
	
		String sql = "select o.*,c.name typename,p.sex,p.age,p.married,p.address,p.income,p.banknum,"
				+ " p.otherincome,p.profession,p.linkname1,p.linkname2,p.linkphone1,"
				+ " p.linkphone2,p.linkReation1,p.linkReation2,p.insuranceid,p.insurancepwd,p.fundid,p.fundpwd,co.name companyName "
				+ " from fk_orderInfo o "
				+ " left join fk_personinfo p on o.personinfo_code=p.code"
				+ " left join sys_type c on o.thetype=c.code "
				+ " left join company_order co on o.sub_entity_id=co.sub_entity_id "
				+ " where o.code=?";
		return mysqlSpringJdbcBaseDao.queryForMap(sql, code);
	}

	@Override
	public List<Map<String, Object>> findTheSamePersonOrderIdsByCode(String code) {
		String sql = "select o.* from fk_orderInfo o where card_num = ( select card_num from fk_orderInfo where  code=? and o.sub_entity_id=sub_entity_id) order by createtime desc";
		return mysqlSpringJdbcBaseDao.queryForList(sql, code);
	}

	@Override
	public boolean manualAudit(Map<String, Object> data) {
		Map<String,Object> where = new HashMap<String, Object>();
		where.put("code", (String)data.remove("code"));
		Update(data, "fk_orderinfo", where);
		return false;
	}

	@Override
	public Map<String, Object> getByCode(String code) {
		String sql = "select o.* from fk_orderInfo o where o.code=?";
		return mysqlSpringJdbcBaseDao.queryForMap(sql, code);
	}

	@Override
	public void updateOrderStatus(String orderId,String status1,String biz_range){
		String sql = " update fk_orderinfo set status1 = ? , biz_range = ? where code = ? ";
		mysqlSpringJdbcBaseDao.update(sql, new Object[]{status1,biz_range,orderId});
	}
	
}
