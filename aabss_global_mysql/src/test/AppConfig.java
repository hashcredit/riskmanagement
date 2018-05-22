//package test;
//
//import org.apache.commons.dbcp.BasicDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@ComponentScan("com")
////@ComponentScan(basePackages = "com.coderli.shurnim.*.biz")
////@Import(DataSourceConfig.class)
////@PropertySource()
//@EnableTransactionManagement
//public class AppConfig {
//
// @Bean
// public BasicDataSource dataSource() {
//	 BasicDataSource dataSource=new BasicDataSource();
//	 dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//	 dataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8");
//	 dataSource.setUsername("root");
//	 dataSource.setPassword("");
//     return dataSource;
// }
//
//// @Bean
//// public DataSourceTransactionManager mainTransactionManager() {
////	 DataSourceTransactionManager mainTransactionManager=new DataSourceTransactionManager();
////	 mainTransactionManager.setDataSource(dataSource());
////     return mainTransactionManager;
//// }
//// @Bean
//// CacheManager cacheManager() {
////     return new RedisCacheManager(redisTemplate());
//// }
//
//}