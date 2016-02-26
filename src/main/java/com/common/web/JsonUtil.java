package com.common.web;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import com.common.exception.DataFormatInvalidException;

public class JsonUtil {
	public static void main(String[] args){
		try {
			JSONObject json = new JSONObject("{\"Strategy\":\"LSXG\",\"Date\":\"2014-11-06\",\"SpanDays\":\"40\",\"ShortestDaysToLastHighest\":\"20\",\"LongestDaysToLastHighest\":\"40\"}");
			String className = "com.bgj.strategy.zjxg.ZJXGInputBean";
			Object o = converJsonToBean(json, className);
			System.out.println(o);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Object converJsonToBean(JSONObject jobJO, String beanClassName)
			throws ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, JSONException, ParseException,
			DataFormatInvalidException {
		if (beanClassName.equals("java.lang.String")) {
			Iterator keys = jobJO.keys();
			if (keys.hasNext()) {
				String name = (String) keys.next();
				return jobJO.getString(name);
			}
		}
		if (beanClassName.equals("int")) {
			Iterator keys = jobJO.keys();
			if (keys.hasNext()) {
				String field = (String) keys.next();
				Object value = jobJO.get(field);
				if (value != null && !value.equals("")) {
					Integer iValue = JsonUtil.parseInteger(value, field,
							field);
					return iValue;
				}
//				String name = (String) keys.next();
//				String value = jobJO.getString(name);
//				Integer iValue = JsonUtil.parseInteger(value, name,
//						beanClassName);
//				return iValue;
			}
		}
		if (beanClassName.equals("long")) {
			Iterator keys = jobJO.keys();
			if (keys.hasNext()) {
				String name = (String) keys.next();
				String value = jobJO.getString(name);
				Long lValue = JsonUtil.parseLong(value, name, beanClassName);
				return lValue;
			}
		}
		if (beanClassName.equals("boolean")) {
			Iterator keys = jobJO.keys();
			if (keys.hasNext()) {
				String name = (String) keys.next();
				Object value = jobJO.get(name);
				Boolean bValue = JsonUtil.parseBoolean(value, name,
						beanClassName);
				return bValue;
			}

		}
		if (beanClassName.equals("double")) {
			Iterator keys = jobJO.keys();
			if (keys.hasNext()) {
				String name = (String) keys.next();
				String value = jobJO.getString(name);
				Double dValue = JsonUtil
						.parseDouble(value, name, beanClassName);
				return dValue;
			}
		}
		if (beanClassName.equals("java.util.Date")) {
			Iterator keys = jobJO.keys();
			if (keys.hasNext()) {
				String name = (String) keys.next();
				String value = jobJO.getString(name);
				Date dValue = JsonUtil.parseDate(value, name, beanClassName);
				return dValue;
			}
		}
		Class c = Class.forName(beanClassName);
		Constructor con = c.getConstructor(null);
		Object o = con.newInstance(null);
		if(jobJO == null){
			return o;
		}
		Method[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method setMethod = methods[i];
			if (setMethod.getName().indexOf("set") == 0) {
				String field = setMethod.getName().substring(3);
				Class parameterType = setMethod.getParameterTypes()[0];
				if (parameterType.getName().equals("java.lang.String")) {
					String value = (String) jobJO.get(field);
					if (value != null && !value.equals("")) {
						setMethod.invoke(o, new String[] { value });
					}
				} else if (parameterType.getName().equals("double")) {
					Object value = jobJO.get(field);
					if (value != null && !value.equals("")) {
						Double dValue = JsonUtil.parseDouble(value, field,
								parameterType.getName());
						setMethod.invoke(o, new Double[] { dValue });
					}
				} else if (parameterType.getName().equals("float")) {
					Object value = jobJO.get(field);
					if (value != null && !value.equals("")) {
						Float fValue = JsonUtil.parseFloat(value, field,
								parameterType.getName());
						setMethod.invoke(o, new Float[] { fValue });
					}
				} else if (parameterType.getName().equals("long")) {
					Object value = jobJO.get(field);
					if (value != null && !value.equals("")) {
						Long lValue = JsonUtil.parseLong(value, field,
								parameterType.getName());
						setMethod.invoke(o, new Long[] { lValue });
					}
				} else if (parameterType.getName().equals("int")) {
					Object value = jobJO.get(field);
					if (value != null && !value.equals("")) {
						Integer iValue = JsonUtil.parseInteger(value, field,
								parameterType.getName());
						setMethod.invoke(o, new Integer[] { iValue });
					}
				} else if (parameterType.getName().equals("byte")) {
					Object value = jobJO.get(field);
					if (value != null && !value.equals("")) {
						Byte bValue = JsonUtil.parseByte(value, field,
								parameterType.getName());
						setMethod.invoke(o, new Byte[] { bValue });
					}
				} else if (parameterType.getName().equals("boolean")) {
					Object value = jobJO.get(field);
					if (value != null && !value.equals("")) {
						Boolean boValue = JsonUtil.parseBoolean(value, field,
								parameterType.getName());
						setMethod.invoke(o, new Boolean[] { boValue });
					}
				} else if (parameterType.getName().equals("java.util.Date")) {
					String value = (String) jobJO.get(field);
					if (value != null && !value.equals("")) {
						Date dValue = JsonUtil.parseDate(value, field,
								parameterType.getName());
						setMethod.invoke(o, new Date[] { dValue });
					}
				} else if (parameterType.getName().equals("java.util.List")) {
					String className = (String) BeanConfiguration.getInstance()
							.getProperty(field);
					JSONArray value = (JSONArray) jobJO.get(field);
					List list = new ArrayList();
					for (int j = 0; j < value.length(); j++) {
						JSONObject each = (JSONObject) value.get(j);
						Object eachObject = converJsonToBean(each, className);
						list.add(eachObject);
					}
					setMethod.invoke(o, new List[] { list });
				} else {
					String className = parameterType.getName();
					JSONObject value = (JSONObject) jobJO.get(field);
					Object eachObject = converJsonToBean(value, className);
					setMethod.invoke(o, new Object[] { eachObject });
				}
			}
		}
		return o;
	}

	private static Double parseDouble(Object value, String field, String type)
			throws DataFormatInvalidException {
		Double dValue = null;
		try {
			dValue = Double.valueOf(value.toString());
		} catch (Exception ex) {
			throw new DataFormatInvalidException(field, type);
		}
		return dValue;
	}

	private static Float parseFloat(Object value, String field, String type)
			throws DataFormatInvalidException {
		Float fValue = null;
		try {
			fValue = Float.valueOf(value.toString());
		} catch (Exception ex) {
			throw new DataFormatInvalidException(field, type);
		}
		return fValue;
	}

	private static Long parseLong(Object value, String field, String type)
			throws DataFormatInvalidException {
		Long lValue = null;
		try {
			lValue = Long.valueOf(value.toString());
		} catch (Exception ex) {
			throw new DataFormatInvalidException(field, type);
		}
		return lValue;
	}

	private static Integer parseInteger(Object value, String field, String type)
			throws DataFormatInvalidException {
		Integer iValue = null;
		try {
			iValue = Integer.valueOf(value.toString());
		} catch (Exception ex) {
			throw new DataFormatInvalidException(field, type);
		}
		return iValue;
	}

	private static Byte parseByte(Object value, String field, String type)
			throws DataFormatInvalidException {
		Byte bValue = null;
		try {
			bValue = Byte.valueOf(value.toString());
		} catch (Exception ex) {
			throw new DataFormatInvalidException(field, type);
		}
		return bValue;
	}

	private static Boolean parseBoolean(Object value, String field, String type)
			throws DataFormatInvalidException {
		Boolean boValue = null;
		if (!"true".equals(value.toString()) && !"false".equals(value.toString())) {
			throw new DataFormatInvalidException(field, type);
		}
		boValue = Boolean.valueOf(value.toString());
		return boValue;
	}

	public static Date parseDate(Object value, String field, String type)
			throws DataFormatInvalidException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dValue = null;
		try {
			dValue = sdf.parse(value.toString());
		} catch (Exception ex) {
			throw new DataFormatInvalidException(field, type);
		}
		return dValue;
	}

}
