package com.newdumai.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

public class SqlHelper {

	private Logger log = Logger.getLogger(this.getClass());
	
	@Test
	public void doTest(){
		String tableName = "fk_orderInfo";
		String names [] ={"name","code","age"};
		Map<String,Object>name_valueMap = new HashMap<String,Object>();
		name_valueMap.put("name", "chang");
		name_valueMap.put("code", "123");
		name_valueMap.put("age", 12);
		name_valueMap.put("age2", 12);
		name_valueMap.put("age3", 12);
		name_valueMap.put("sex", "F");
		String  sql = SqlHelper.getWhereSqlByParams(name_valueMap, names);
		log.info(sql);
	}
	
	
	/**
	 * 查询 某个表的记录 根据参数
	 * @param tableName
	 * @param name_valueMap
	 * @param names
	 * @return select * from tableName where 1=1 and  name = name_valueMap.get(name) and ...
	 */
	public static String getSelectSqlByParams(String tableName,Map<String,Object>name_valueMap,String ... names){
		StringBuilder sb = new StringBuilder();
		sb.append("select * from "+tableName+" where 1=1 ");
		if(!CollectionUtils.sizeIsEmpty(names)){
			for(String name :names){
				Object value = name_valueMap.get(name);
				try {
					if((value instanceof String)&&StringUtils.isNotEmpty((String) value)){
						sb.append(" and "+name+" = '"+value+"' ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 *  仅仅给sql语句构建查询参数
	 * @param name_valueMap
	 * @param names
	 * @return select ... where 1=1 and  name = name_valueMap.get(name) and ...
	 */
	public static String getWhereSqlByParams(Map<String,Object>name_valueMap,String ... names){
		StringBuilder sb = new StringBuilder();
		sb.append(" where 1=1 ");
		if(!CollectionUtils.sizeIsEmpty(names)){
			for(String name :names){
				Object value = name_valueMap.get(name);
				try {
					if((value instanceof String)&&StringUtils.isNotEmpty((String) value)){
						sb.append(" and "+name+" = '"+value+"' ");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}
	
	public String getPageSql(String table,String limit,boolean is_total){
		StringBuilder sb = new StringBuilder();                                                                                                                                                                                                                                                                                                        
		sb.append("  (  ");  
		sb.append(table);
		sb.append("  )  ");  
		StringBuilder sb2 = new StringBuilder();
		if(is_total){
			sb2.append(" select count(*) as total from  ");    
			sb2.append(sb);
		}else{
			sb2.append(" select * from  ");   
			sb2.append(sb);
			sb.append(limit);
		}
		return sb2.toString();
	}
	
	public String getLimitUseAtSelectPage(Map<String,Object>map) {
		String limit = " limit 0,10 ";
		try {
			String page = (String) map.get("page");
			String rows = (String) map.get("rows");
			if (StringUtils.isNotEmpty(page)&&StringUtils.isNotEmpty(rows)) {
				int strart = (Integer.parseInt(page) - 1) * Integer.parseInt(rows);
				limit = " limit " + strart + " , " + rows;
			} 
		} catch (Exception e) {
			
		}
		return limit;
	}
	
	
}
