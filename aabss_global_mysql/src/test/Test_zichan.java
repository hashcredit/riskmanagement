package test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.newdumai.global.dao.Zichan_BaseDao;
import com.newdumai.util.Dumai_zichanConfig;



public class Test_zichan {

	public static void main(String[] args) throws Exception {
		findAll();
	}

	public static void findAll() {
		System.out.println(getBaseDao().executeSelectSql("select * from sys_dict_info"));
	}

	@SuppressWarnings("resource")
	public static Zichan_BaseDao getBaseDao() {
		return new AnnotationConfigApplicationContext(Dumai_zichanConfig.class).getBean("zichan_BaseDao", Zichan_BaseDao.class);
	}
	
}
