package com.newdumai.util;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ComponentScan("com.newdumai.**.impl")
@ImportResource("classpath:/dumai_source/db_mysqlSpringJdbcBaseDao.xml")  
public class Dumai_sourceConfig {
}
