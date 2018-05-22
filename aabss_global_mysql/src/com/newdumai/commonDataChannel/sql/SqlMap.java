package com.newdumai.commonDataChannel.sql;

import java.util.HashMap;
import java.util.Map;

public class SqlMap {

	public static Map<String, String> sqlMap = new HashMap<String, String>();

	static {

		sqlMap.put("get_interface_detail_logs"," SELECT dm_3rd_interface.`name` as 'interface_name', dm_3rd_interface.dm_3rd_interface_company_name as 'company_name', dm_3rd_interface_detail.in_para , dm_3rd_interface_detail.result , dm_3rd_interface.description, dm_3rd_interface_detail.opttime FROM dm_3rd_interface_detail  INNER JOIN dm_3rd_interface ON dm_3rd_interface.`code` = dm_3rd_interface_detail.dm_3rd_interface_code ORDER BY `opttime` DESC LIMIT 0,200 ");

	}

}
