package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.newdumai.dumai_data.dm_3rd_interface.Dm_3rd_interfaceService;
import com.newdumai.global.dao.Dumai_sourceBaseDao;
import com.newdumai.util.Dumai_sourceConfig;
import com.newdumai.util.JsonToMap;

public class TestAllDS {
	private AnnotationConfigApplicationContext ctx;
	
	@Before
	public void init() {
		ctx = new AnnotationConfigApplicationContext(Dumai_sourceConfig.class);
	}
	
	@After
	public void destroy() {
		ctx.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> lines = IOUtils.readLines(new FileInputStream("D:/Work/数据源测试in_para.txt"), "UTF-8");
		Gson gson3 = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		String dest = "D:/Work/in_paras/";
		for (String line : lines) {
			String[] arr = line.split("\t");
			String json = arr.length == 3 ? arr[2] : "";
			if (!json.isEmpty()) {
				JsonElement je = jp.parse(json);
				json = gson3.toJson(je);
			}
			
			IOUtils.write(json, new FileOutputStream(dest + arr[0] + ".json"));
		}
	}
	
	@Test
	public void testDS() {
		String sql =
		" SELECT "+
		"	t.code,t.name,dd.in_para,dd.result "+
		" from "+
		"	dm_3rd_interface_detail dd"+
		" RIGHT JOIN "+
		"	( "+
		"		SELECT "+	
		"			d.*, ( "+
		"				SELECT "+
		"					dd.code "+
		"				FROM "+
		"					dm_3rd_interface_detail dd "+
		"				where dd.dm_3rd_interface_code=d.`code` ORDER BY ID desc LIMIT 0,1 "+
		"			) ddcode "+
		"		FROM "+
		"			dm_3rd_interface d where d.is_online='1' and is_able='1' "+
		"	) t on dd.`code`= t.ddcode";
		Dm_3rd_interfaceService dm_3rd_interfaceService = ctx.getBean(Dm_3rd_interfaceService.class);
		Dumai_sourceBaseDao dumai_sourceBaseDao = (Dumai_sourceBaseDao) ctx.getBean("dumai_sourceBaseDao");
		List<Map<String, Object>> dm_3rd_interfaceList = dumai_sourceBaseDao.queryForList(sql);
		System.out.println(dm_3rd_interfaceList);
		List<Map<String, Object>> problem_dm_3rd_interfaceList = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : dm_3rd_interfaceList) {
			String dm_3rd_interface_code = (String) map.get("code");
			try {
				String json = (String) map.get("in_para");
				Map<String,Object> in_para = JsonToMap.gson2Map(json);
				String result = dm_3rd_interfaceService.testDS0(dm_3rd_interface_code, in_para);
				if(result==null){
					problem_dm_3rd_interfaceList.add(map);
				}
			} catch (Exception e) {
				problem_dm_3rd_interfaceList.add(map);
				continue;
			}
		}
		System.out.println("有问题的数据源:" + problem_dm_3rd_interfaceList);
	}
}
