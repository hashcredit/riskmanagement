package com.newdumai.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InvokeMethod {
	public static String invoke(String classNameAndMethodName,Map<String, Object> params) throws Exception {
		java.lang.String[] split = classNameAndMethodName.split("#");
		Class<?> onwClass = null;
		Method m1 = null;
		onwClass = Class.forName(split[0]);
		m1 = onwClass.getDeclaredMethod(split[1], Map.class);
		return (java.lang.String) m1.invoke(onwClass, params);
	}
	
	/**
	 * 调用静态方法
	 * @param classNameAndMethodName package.classname#methodName 即：类全限定名#方法名
	 * @param classes 参数类型列表(声明时的类型，子类、实现类不会匹配)
	 * @param args 参数列表
	 * @return 执行方法的返回结果(如果方法返回类型为void则返回null)
	 * @throws Exception
	 */
	public static Object invoke(String classNameAndMethodName,Class<?>[] classes, Object... args) throws Exception {
		java.lang.String[] split = classNameAndMethodName.split("#");
		Method method = null;
		Class<?> onwClass = Class.forName(split[0]);
		method = onwClass.getDeclaredMethod(split[1], classes);
		method.setAccessible(true);
		return method.invoke(null, args);
	}
	
	public static void main(String[] args) {
		String s = "com.newdumai.setting.interface_source.in_interface.yixin.YiXinOutParamFormatter#aa";
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("result", "sdfasfasdf");
		try {
			invoke(s,params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
