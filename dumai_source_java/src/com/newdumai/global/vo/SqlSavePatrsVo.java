package com.newdumai.global.vo;

import java.util.List;

/** 
 */
public class SqlSavePatrsVo {

	private String sql;
	private StringBuilder colum;
	private StringBuilder mark;
	private List<Object> val;

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public StringBuilder getColum() {
		return colum;
	}

	public void setColum(StringBuilder colum) {
		this.colum = colum;
	}

	public StringBuilder getMark() {
		return mark;
	}

	public void setMark(StringBuilder mark) {
		this.mark = mark;
	}

	public List<Object> getVal() {
		return val;
	}

	public void setVal(List<Object> val) {
		this.val = val;
	}
}
