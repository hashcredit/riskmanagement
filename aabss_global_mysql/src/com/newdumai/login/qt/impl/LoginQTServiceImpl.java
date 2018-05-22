package com.newdumai.login.qt.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.login.qt.LoginQTService;

@Service("loginQTService")
public class LoginQTServiceImpl extends BaseServiceImpl implements LoginQTService {

	@Override
	public Map<String, Object> getByUserName(String username) {
		List<Map<String, Object>> users = super.mysqlSpringJdbcBaseDao.queryForList("SELECT * FROM sys_user  WHERE user_name = ?",username);
		if(users != null && users.size() > 0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public void addLog(Map<String, Object> params) {
		add(params, "sys_log");
	}

	@Override
	public boolean inWhiteIps(String ip, String subEntityId) {
		Map<String, Object> data = super.mysqlSpringJdbcBaseDao.queryForMap("SELECT white_ips FROM company_order  WHERE sub_entity_id = ?",subEntityId);
		if(data==null) return false;
		
		String whiteIps = (String) data.get("white_ips");
		if(whiteIps!=null){
			String[] ips = whiteIps.split(",");
			for(String theIp : ips){
				if(theIp.equals(ip)){
					return true;
				}
			}
		}
		return false;
	}
}
