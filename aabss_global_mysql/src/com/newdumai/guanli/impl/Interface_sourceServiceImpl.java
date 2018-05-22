//package com.newdumai.guanli.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//
//import com.google.gson.Gson;
//import com.newdumai.global.service.impl.BaseServiceImpl;
//import com.newdumai.guanli.Interface_sourceService;
//import com.newdumai.util.PageData;
//
//
//@Service("interface_sourceService")
//public class Interface_sourceServiceImpl extends BaseServiceImpl implements Interface_sourceService {
//	@Override
//	public String list() {
//		PageData p=new PageData();
//		p.setCurrPage(0);
//		p.setPageSize(20);
//		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("total", findByPageCount("sys_interface_source", "", p));
//		data.put("rows", findByPage2("sys_interface_source", "", p));
//		return new Gson().toJson(data);
//	}
//
//	@Override
//	public int upadte_Interface_sourceService(Map<String, Object> para) {
//		Map<String, Object> where=new HashMap<String, Object>();
//		where.put("code", para.get("code"));
//		para.remove("code");
//		return Update(para, "sys_interface_source_para", where);
//	}
//}
