package com.newdumai.global.vo;

import java.util.List;

/** 
 */
public class SqlUpdatePatrsVo {

	private String sql;
	private List<Object> val;
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<Object> getVal() {
		return val;
	}
	public void setVal(List<Object> val) {
		this.val = val;
	}

}
