package com.grgbanking.core.utils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 实体与Map互转工具
 *
 * @author kevin
 * @date 2018/10/31
 */
public class Pojo2MapUtil {

	public static <E> E map2E(Map<String, Object> item, Class<?> clasz,
							  boolean allownull) throws Exception {
		E obj;
		Field[] fields = clasz.getFields();
		obj = (E) clasz.newInstance();

		for (Field field : fields) {

		  Object value = item.get(field.getName());
		  value = ConvertUtil.castFromObject(value, field.getType());
		  if (!allownull && value == null) {
			  continue;
		  }
		  setFieldValue(clasz, obj, field.getName(), value);
		}
		fields = clasz.getDeclaredFields();

		for (Field field : fields) {

			Object value = item.get(field.getName());
			value = ConvertUtil.castFromObject(value, field.getType());
				if (!allownull && value == null) {
				continue;
			}
			setFieldValue(clasz, obj, field.getName(), value);

		}
		return obj;
  	}

	public static <E> E map2E(Map<String, Object> item, E obj, boolean allownull)
			throws Exception {
		if (obj == null)
			return obj;
		Field[] fields = obj.getClass().getFields();
		for (Field field : fields) {
			Object value = item.get(field.getName());
			value = ConvertUtil.castFromObject(value, field.getType());
			if (!allownull && value == null) {
				continue;
			}
			setFieldValue(obj.getClass(), obj, field.getName(), value);
		}
		fields = obj.getClass().getDeclaredFields();

		for (Field field : fields) {
			Object value = item.get(field.getName());
			value = ConvertUtil.castFromObject(value, field.getType());
			if (!allownull && value == null) {
				continue;
			}
			setFieldValue(obj.getClass(), obj, field.getName(), value);
		}
		return obj;
	}

    public static <E> void printObject(E obj, Class<?> clasz) throws Exception {
		Field[] fields = clasz.getFields();
		for (Field field : fields) {
			Object value = getFieldValue(clasz, field.getName(), obj);
			String classname = value.getClass().toString();
			if (classname.indexOf("grgbanking") > 0) {
				printObject(value, value.getClass());

			}
		}
		fields = clasz.getDeclaredFields();
    	for (Field field : fields) {

			Object value = getFieldValue(clasz, field.getName(), obj);
			String classname = value.getClass().toString();
			if (classname.indexOf("grgbanking") > 0&&classname.indexOf("[Lcom.")<0) {
				printObject(value, value.getClass());
			}
		}
  }

	public static <E, V> V E2V(E obj, Class<E> eclasz, Class<V> vclasz) throws Exception{
	    Field[] fields = vclasz.getFields();
	    V newobj;
		newobj = (V) vclasz.newInstance();
	    for (Field field : fields) {
			Object value = getFieldValue(eclasz, field.getName(), obj);
			setFieldValue(vclasz, newobj, field.getName(), value);
		}
	    fields = vclasz.getDeclaredFields();
	
	    for (Field field : fields) {
			Object value = getFieldValue(eclasz, field.getName(), obj);
			setFieldValue(vclasz, newobj, field.getName(), value);
		}
		return newobj;
  }

	public static Object getFieldValue(Class<?> clasz, String field, Object bean) throws Exception{
		try {
			Field f = clasz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(bean);
		} catch (NoSuchFieldException e) {
			Field f = clasz.getField(field);
			f.setAccessible(true);
			return f.get(bean);
		}

	}

	public static void setFieldValue(Class<?> clasz, Object obj, String field,
			Object value) throws Exception{
		try {
			Field f = clasz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(obj, value);
		} catch (NoSuchFieldException e) {
			Field f = clasz.getField(field);
			f.setAccessible(true);
			f.set(obj, value);
		}
	}
}