package test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.newdumai.busi.busi.service.RoleService;
import com.newdumai.util.Dumai_newConfig;


public class RoleTest {

	public static void main(String[] args) throws Exception {
		findrole();
	}

	public static void findrole() {
		System.out.println(getBaseService().list(""));
	}

	@SuppressWarnings("resource")
	public static RoleService getBaseService() {
		return new AnnotationConfigApplicationContext(Dumai_newConfig.class).getBean("roleService", RoleService.class);
	}
	
}
