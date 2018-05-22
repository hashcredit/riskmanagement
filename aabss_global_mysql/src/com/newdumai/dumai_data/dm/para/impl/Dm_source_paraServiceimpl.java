package com.newdumai.dumai_data.dm.para.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.newdumai.dumai_data.dm.para.Dm_source_paraService;
import com.newdumai.global.service.impl.BaseServiceImpl;
import com.newdumai.global.service.impl.Dumai_sourceBaseServiceImpl;

@Service("dm_sourceService_para")
public class Dm_source_paraServiceimpl extends Dumai_sourceBaseServiceImpl implements Dm_source_paraService {
	
	@Override
	public String list(Map<String, Object> map) {
		Map<String, Object> condition = getCondition_list(map);
		return listPageBase(condition,gen_list_1(condition.get("condition").toString()),gen_list_2(condition.get("condition").toString(),getLimitUseAtSelectPage(map)));
	}

	@Override
	public int add_Interface_sourceService_para(Map<String, Object> para) {
		return add(para,"dm_source_para");
	}

	@Override
	public String para_toUpdate(String interface_source_code) {
		return super.dumai_sourceBaseDao.executeSelectSql(gen_para_toUpdate(interface_source_code));
	}

	@Override
	public int upadte_Interface_sourceService_para(Map<String, Object> para) {
		Map<String, Object> where=new HashMap<String, Object>();
		where.put("code", para.get("code"));
		para.remove("code");
		return Update(para, "dm_source_para", where);
	}
	
	private String gen_list_1(String condition) {
		return "SELECT count(*) FROM dm_source_para WHERE 1=1 "+condition;
	}
	private String gen_list_2(String condition,String limit) {
		
		return "SELECT * FROM dm_source_para WHERE 1=1 "+condition + " order by type" +limit ;
	}
	public Map<String, Object> getCondition_list(Map<String, Object> map){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Object> list=new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		String code = (String)map.get("code");
		sb.append(" and dm_source_code=?");
		list.add(code);
		data.put("condition", sb.toString());
		data.put("args", list.toArray());
		return data;
	}

	private String gen_para_toUpdate(String interface_source_code) {
		return "SELECT * FROM dm_source_para WHERE code='"+interface_source_code+"'";
	}

	@Override
	public List<Map<String, Object>> getAllParams(String interface_source_code, String type) {
		return super.dumai_sourceBaseDao.queryForList("select * from dm_3rd_interface_para where dm_3rd_interface_code=? and type=?", interface_source_code, type);
	}

	@Override
	public void insertParamsMatch(List<Map<String, Object>> dataParams) {
		Object[][] args = new Object[dataParams.size()][];
		int i = 0;
		for(Map<String, Object> map : dataParams){
			Object[] arg = new Object[6];
			arg[0] = UUID.randomUUID().toString();
			arg[1] = map.get("dm_source_code");
			arg[2] = map.get("dm_source_para_code");
			arg[3] = map.get("dm_3rd_interface_code");
			arg[4] = map.get("dm_3rd_interface_para_code");
			arg[5] = map.get("base_condition");
			args[i++] = arg;
		}
		String sql = "INSERT INTO dm_source_para__dm_3rd_interface_para (code,dm_source_code,dm_source_para_code,dm_3rd_interface_code,dm_3rd_interface_para_code,base_condition) VALUES (?,?,?,?,?,?)";
		super.dumai_sourceBaseDao.batchInsert(sql, args);
	}

	@Override
	public void deleteParamsMatch(String paramCode) {
		String sql = "DELETE FROM dm_source_para__dm_3rd_interface_para WHERE dm_source_para_code=?";
		super.dumai_sourceBaseDao.delete(sql, paramCode);
	}

	@Override
	public List<Map<String, Object>> getSourceParamList(String paramCode) {
		return super.dumai_sourceBaseDao.queryForList("SELECT * FROM dm_source_para__dm_3rd_interface_para WHERE dm_source_para_code=?", paramCode);
	}

	@Override
	public int delByCode(String code) {
		String sql = "DELETE from dm_source_para WHERE code = ?";
		return super.dumai_sourceBaseDao.delete(sql, code);
	}

	@Override
	public List<Map<String, Object>> getOutParasByDmSourceCode(String dm_source_code) {
		return dumai_sourceBaseDao.queryForList("select * from dm_source_para  where type='1' and name <> '--' and dm_source_code=?",dm_source_code);
	}
	
	@Override
	public Map<String, Object> getAllDmSourceOutParas() {
		List<Map<String, Object>> dm_source_paraList = dumai_sourceBaseDao.queryForList("select * from dm_source_para  where type='1' and name <> '--' ");
		Map<String,Object> outParas = new HashMap<String,Object>();
		for(Map<String,Object> dm_source_map : dm_source_paraList){
			outParas.put((String)dm_source_map.get("code"), (String)dm_source_map.get("name"));
		}
		return outParas;
	}

	@Override
	public void saveParamsMatch(String paramCode, List<Map<String, Object>> dataParams) {
		deleteParamsMatch(paramCode);
		insertParamsMatch(dataParams);
	}
	
	
}
