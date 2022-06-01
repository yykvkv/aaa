package com.grgbanking.core.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 反射方法文件
 *
 * @author kevin
 * @date 2018/08/09
 */

public class ExecClassMethod {

	public static Object execMethod(String sClassNameMethod,
									Object defaultRetrun, Object... strArgs) throws Exception {
		if(sClassNameMethod==null||sClassNameMethod.equals("")||sClassNameMethod.indexOf(":")<0) {
			return defaultRetrun;
		}
		String[] ss = sClassNameMethod.split(":");
		Object object = invokeMethod(creatObject(ss[0]), ss[1], strArgs);
		return object == null ? defaultRetrun : object;
	}

	/**
	 * 创建普通对象。 触发类名称
	 *
	 * @param sClassName
	 *            类全名，含包名
	 * @return
	 */
	public static Object creatObject(String sClassName) {
		// String ClassName = _z.getCLASS_NAME();
		Object ret = null;
		if (sClassName == null || sClassName.length() == 0)
			return ret;
		Class clazz = null;
		try {
			clazz = Class.forName(sClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return ret;
		}
		try {
			ret = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 创建普通对象。 触发类方法名称

	 *            类全名，含包名
	 * @return
	 * @throws
	 */
	public static Object invokeMethod(Object object, String sMethodName,
									  Object... strArgs) throws Exception {
		// 创建组件的方法对象
		Method method = null;
		// 调用该对象的方法
		Object result = null;

		if (strArgs != null) {
			Class[] classes=new Class[strArgs.length];
			for(int i=0;i<strArgs.length;i++){
				classes[i]=strArgs[i].getClass();
			}

			try {
				method = object.getClass().getMethod(sMethodName, classes);
			} catch (Exception e) {
				for(int i=0;i<strArgs.length;i++){
					// 如果当前类型方法没找到，Class实体转换
					if (classes[i].isAssignableFrom(HashMap.class)) {
						classes[i] = Map.class;
					}if (classes[i].isAssignableFrom(TreeMap.class)) {
						classes[i] = Map.class;
					} else if (classes[i].isAssignableFrom(ArrayList.class)) {
						classes[i] = ArrayList.class;
					}
				}
				try{
					method = object.getClass().getMethod(sMethodName, classes);
				}catch(Exception ae){
					for(int i=0;i<strArgs.length;i++){
						classes[i] = Object.class;
					}
					method = object.getClass().getMethod(sMethodName, classes);
				}
			}
		} else {
			method = object.getClass().getMethod(sMethodName);
		}
		if (method == null) {
			return null;
		}
		if (strArgs != null) {
			result = method.invoke(object, strArgs);
		} else {
			result = method.invoke(object);
		}
		// 组件方法要求返回boolean值
		return result;
	}

}
