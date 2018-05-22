package test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.newdumai.loanFront.GenReportService;

public class TestGenReport {
private ClassPathXmlApplicationContext ctx;
	
	@Before
	public void init(){
		ctx = new ClassPathXmlApplicationContext("classpath:test/application.xml");
	}
	
	@After
	public void destroy(){
		ctx.close();
	}
	@Test
	public void test(){
		GenReportService genReportService = ctx.getBean(GenReportService.class);
	//	GenReportService genReportService = (GenReportService) ctx.getBean("genReportService2");
		String report = genReportService.Report("1", "01f883a9-4498-4cfc-8659-ef61bc0e6cd7");
		System.out.println(report);
	}
}
