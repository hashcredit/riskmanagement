package com.newdumai.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * @author zfc
 * 
 */
public class DeviceCache {
	private static Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	public static int getSize() {
		return map.size();
	}

	public static Object getObject(String key) {
		return map.get(key);
	}

	public static void setObject(String key, String value) {
		map.put(key, value);
	}

	public static Map<String, Object> getAll(String key) {
		return map;
	}
}