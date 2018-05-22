package com.newdumai.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PageData implements Serializable {
	private static final long serialVersionUID = 1L;

	private int currPage;// 褰撳墠椤�
	private int pageSize;// 姣忛〉璁板綍鏁�
	private int total;// 鎬昏褰曟暟
	private String rows;
	public int getCurrPage() {
		return currPage;
	}
	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getRows() {
		return rows;
	}
	public void setRows(String rows) {
		this.rows = rows;
	}
}
