//package test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//import com.aabss.demo.service.BusiService;
//import com.aabss.util.MysqlSpringJdbcConfig;
//
//
//public class Test2 {
//
//	public static void main(String[] args) throws Exception {
//		findAll();
//		save();
//		findAll();
//		update();
//		findAll();
//		delete();
//		findAll();
//	}
//
//	public static void findAll() {
//		System.out.println(getBaseService().findAll());
//	}
//	public static void save() {
//		Map<String, String> map=new HashMap<String, String>();
//		map.put("msg", "jack");
//		getBaseService().save(map);
//	}
//	public static void update() {
//		Map<String, String> map=new HashMap<String, String>();
//		map.put("msg", "jack");
//		getBaseService().update(map);
//	}
//	public static void delete() {
//		Map<String, String> map=new HashMap<String, String>();
//		map.put("msg", "tom");
//		getBaseService().delete(map);
//	}
//
//	@SuppressWarnings("resource")
//	public static BusiService getBaseService() {
//		return new AnnotationConfigApplicationContext(MysqlSpringJdbcConfig.class).getBean("busiService", BusiService.class);
//	}
//	
//}
