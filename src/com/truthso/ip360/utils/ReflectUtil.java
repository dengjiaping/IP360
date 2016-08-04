package com.truthso.ip360.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

public class ReflectUtil {

	/**
	 * 根据属性名获取属性值,暂不支持对子级不集合的数据获取
	 * */
	public static Object getFieldValueByName(String fieldName, Object o) {
		int fieldIndex = fieldName.indexOf(".");
		String tempName = "";
		if (fieldIndex > 0) {
			tempName = fieldName.substring(0, fieldIndex);
		} else {
			tempName = fieldName;
		}
		try {
			String firstLetter = tempName.substring(0, 1).toUpperCase();
			String getter = "get" + firstLetter + tempName.substring(1);
			Method method = o.getClass().getMethod(getter, new Class[] {});
			Object value = method.invoke(o, new Object[] {});
			if (tempName == fieldName) {
				return value;
			} else {
				return getFieldValueByName(fieldName.substring(fieldIndex + 1), value);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ReflectUtil", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 获取属性名数组
	 * */
	public static String[] getFiledName(Object o) {
		Field[] fields = o.getClass().getDeclaredFields();
		String[] fieldNames = new String[fields.length];
		for (int i = 0; i < fields.length; i++) {
			fieldNames[i] = fields[i].getName();
		}
		return fieldNames;
	}

	/**
	 * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
	 */
	public static List<Map<String, Object>> getFiledsInfo(Object obj) {
		Field[] fields = obj.getClass().getDeclaredFields();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> infoMap = null;
		for (int i = 0; i < fields.length; i++) {
			infoMap = new HashMap<String, Object>();
			infoMap.put("type", fields[i].getType().toString());
			infoMap.put("name", fields[i].getName());
			infoMap.put("value", getFieldValueByName(fields[i].getName(), obj));
			list.add(infoMap);
		}
		return list;
	}
	/**
	 * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
	 * @param obj
	 * @return map<String,Map> key=字段名称，value=属性类型(type)，属性名(name)，属性值(value)的map
	 */
	public static Map<String, Map<String, Object>> getFiledsInfoMap(Object obj) {
		if (obj==null) {
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		Map<String, Object> infoMap = null;
		for (int i = 0; i < fields.length; i++) {
			infoMap = new HashMap<String, Object>();
			String fieldName = fields[i].getName();
			infoMap.put("type", fields[i].getType().toString());
			infoMap.put("name", fieldName);
			infoMap.put("value", getFieldValueByName(fields[i].getName(), obj));
			map.put(fieldName, infoMap);
		}
		return map;
	}

	/**
	 * 获取对象的所有属性值，返回一个对象数组
	 * */
	public static Object[] getFiledValues(Object o) {
		String[] fieldNames = getFiledName(o);
		Object[] value = new Object[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			value[i] = getFieldValueByName(fieldNames[i], o);
		}
		return value;
	}
}
