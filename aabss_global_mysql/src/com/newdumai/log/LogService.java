package com.newdumai.log;

import java.util.Map;

public interface LogService {


	String list(Map<String,Object> map);
	String list(Map<String,Object> map,String subEntityId);

}
