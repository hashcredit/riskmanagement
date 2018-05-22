//package test;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.aabss.global.dao.BaseDao;
//import com.aabss.global.service.BaseService;
//import com.aabss.util.MysqlSpringJdbcConfig;
//
//
//public class Test {
//	private static BaseDao mysqlSpringJdbcBaseDao = null;
//	@SuppressWarnings("resource")
//	public static BaseDao getBaseDao() {
//		if (mysqlSpringJdbcBaseDao != null) {
//			return mysqlSpringJdbcBaseDao;
//		} else {
//			AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MysqlSpringJdbcConfig.class);
//			mysqlSpringJdbcBaseDao = ctx.getBean("springJdbcBaseDao", BaseDao.class);
//			return mysqlSpringJdbcBaseDao;
//		}
//	}
//	@SuppressWarnings("resource")
//	public static BaseService getBaseService() {
//		return new AnnotationConfigApplicationContext(MysqlSpringJdbcConfig.class).getBean("baseService", BaseService.class);
//	}
//	public static void main(String[] args) throws Exception {
//		//new Test().aa();
//		getBaseService().add("INSERT INTO sys_log (msg) VALUES ('aaa222')");
//		System.out.println(getBaseService().find("sys_log"));
//	}
//	
//	@SuppressWarnings("unused")
//	private void aa() throws Exception{
//		insertNode2();
//
//		@SuppressWarnings("resource")
//		BaseService bs=new AnnotationConfigApplicationContext(MysqlSpringJdbcConfig.class).getBean("baseService", BaseService.class);
//		bs.add("INSERT INTO sys_log (msg) VALUES ('aaa')");
//	}
//	@Transactional
//	private void insertNode2() throws Exception {
//		BaseDao sp = getBaseDao();
//		sp.executeSql("INSERT INTO sys_log (msg) VALUES ('aaa')");
//	}
//}
