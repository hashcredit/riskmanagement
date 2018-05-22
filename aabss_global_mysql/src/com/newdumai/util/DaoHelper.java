package com.newdumai.util;

import com.newdumai.global.dao.Dumai_newBaseDao;
import com.newdumai.global.dao.Dumai_sourceBaseDao;

public class DaoHelper {
	
	private static Dumai_newBaseDao mysqlSpringJdbcBaseDao;
	private static Dumai_sourceBaseDao dumai_sourceBaseDao;
	
	public static Dumai_newBaseDao getDumai_newBaseDao(){
		if(mysqlSpringJdbcBaseDao == null ){
			mysqlSpringJdbcBaseDao = (Dumai_newBaseDao) SpringApplicationContextHolder.getBean("mysqlSpringJdbcBaseDao");
		}
		return mysqlSpringJdbcBaseDao;
	}
	
	public static Dumai_sourceBaseDao getDumai_sourceBaseDao(){
		if(dumai_sourceBaseDao == null){
			dumai_sourceBaseDao = (Dumai_sourceBaseDao) SpringApplicationContextHolder.getBean("dumai_sourceBaseDao");
		}
		return dumai_sourceBaseDao;
	}
	
}
