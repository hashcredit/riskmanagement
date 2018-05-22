package com.newdumai.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

public class MapObjUtil {

	/**
	 * 如果确认Map<String, Object>中的值全部为String类型，可把map转换成Map<String, String>
	 * 
	 * @param map
	 * @return
	 * @zgl Jan 18, 2017 2:04:24 PM
	 */
	public static Map<String, String> mapObjToString(Map<String, Object> map) {
		Map<String, String> returnMap = new HashMap<String, String>();
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			String value = "";
			if (null != entry.getValue()) {
				value = String.valueOf(entry.getValue());
			}
			returnMap.put(entry.getKey(), value);
		}
		return returnMap;
	}

	/**
	 * 当map中只有一对key：value时，取得value值
	 * 
	 * @param map
	 * @return
	 * @zgl Jan 19, 2017 9:56:24 AM
	 */
	public static Object getMapValue(Map<String, Object> map) {
		Object result = null;
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			result = entry.getValue();
		}
		return result;
	}

	/**
	 * 放入此map中的数据会根据key进行排序并按照顺序输出 已实现线程同步机制
	 * 
	 * @designed-by tianxinyang
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> getMapSortedByKey() {
		Map<String, Object> map = Collections.synchronizedMap(new TreeMap<String, Object>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				return obj2.compareTo(obj1);
			}
		}));
		return map;
	}

	/**
	 * trim 所有指定的key的值
	 * 
	 * @param <K>
	 * @param <V>
	 * @designed-by tianxinyang
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> trimValueByKey(Map<K, V> map, K... ks) {
		try {
			for (K k : ks) {
				if (map.containsKey(k)) {
					try {
						map.put(k, (V) StringUtils.trim((String) map.get(k)));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
