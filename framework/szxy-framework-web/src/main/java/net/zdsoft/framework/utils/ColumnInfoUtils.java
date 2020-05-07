package net.zdsoft.framework.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.ColumnInfoEntity;

public class ColumnInfoUtils {

	private static final Map<String, Map<String, ColumnInfoEntity>> columnInfoMap = new HashMap<String, Map<String, ColumnInfoEntity>>();
	private static final Map<String, List<String>> entityInfoMap = new HashMap<String, List<String>>();

	/**
	 * 初始化类的ColumnInfo注解，获取字段信息
	 * @param clazz
	 */
	private static void initColumnProperties(Class<?> clazz) {
		Class<?> superClass = clazz;
		String key = superClass.getName();
		List<String> infos = entityInfoMap.get(key);
		if (infos == null) {
			List<Object[]> infos2 = new ArrayList<Object[]>();
			infos = new ArrayList<String>();
			entityInfoMap.put(key, infos);

			while (superClass != null) {
				Set<String> set = new HashSet<String>();
				Method[] methods = superClass.getDeclaredMethods();
				for (Method method : methods) {
					if (method.getModifiers() != Modifier.PUBLIC) {
						continue;
					}
					String name = method.getName();
					if (StringUtils.startsWith(name, "get")) {
						set.add(StringUtils.substringAfter(name, "get").toLowerCase());
					}
				}
				Field[] fields = superClass.getDeclaredFields();
				for (Field field : fields) {
					String name = field.getName();
					if (!set.contains(name.toLowerCase()))
						continue;
					ColumnInfo colInfo = field.getAnnotation(ColumnInfo.class);
					if (colInfo == null)
						continue;
					infos2.add(new Object[] { name, colInfo.displayOrder() });
				}
				superClass = superClass.getSuperclass();
			}

			Collections.sort(infos2, new Comparator<Object[]>() {
				@Override
				public int compare(Object[] o1, Object[] o2) {
					Object do1 = o1[1];
					Object do2 = o2[1];
					if (do1 == null)
						return -1;
					if (do2 == null)
						return 1;
					return ((Integer) do1) - ((Integer) do2);
				}
			});

			for (Object[] os : infos2) {
				infos.add((String) os[0]);
			}
		}
	}

	/**
	 * 初始化类的ColumnInfo注解，获取注解信息，并转化为ColumnInfoEntity
	 * @param clazz
	 */
	private static void initColumnInfos(Class<?> clazz) {
		String key = clazz.getName();
		Map<String, ColumnInfoEntity> infos = columnInfoMap.get(key);
		if (infos == null) {
			infos = new HashMap<String, ColumnInfoEntity>();
			columnInfoMap.put(key, infos);
			Class<?> superClass = clazz;
			while (superClass != null) {
				Field[] fields = superClass.getDeclaredFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(ColumnInfo.class)) {
						ColumnInfo p = field.getAnnotation(ColumnInfo.class);
						ColumnInfoEntity e = new ColumnInfoEntity();
						e.setDisabled(p.disabled());
						e.setDisplayName(p.displayName());
						e.setDisplayOrder(p.displayOrder());
						e.setFitForSchoolType(p.fitForSchoolType());
						e.setFitForUnitClass(p.fitForUnitClass());
						e.setFormat(p.format());
						e.setHide(p.hide());
						e.setLength(p.length());
						e.setMax(p.max());
						e.setMaxLength(p.maxLength());
						e.setMcodeId(p.mcodeId());
						e.setMin(p.min());
						e.setMinLength(p.minLength());
						e.setNullable(p.nullable());
						e.setRegex(p.regex());
						e.setRegexTip(p.regexTip());
						e.setUnitType(p.unitType());
						e.setVsql(p.vsql());
						e.setVtype(p.vtype());
						e.setVselect(p.vselect());
						e.setReadonly(p.readonly());
						infos.put(field.getName(), e);
					}
				}
				superClass = superClass.getSuperclass();
			}
		}
	}

	/**
	 * 复制新的ColumnInfoEntity，用于Action层进行属性的变更
	 * @param map
	 * @param columnName
	 * @return
	 */
	public static Map<String, ColumnInfoEntity> copyColumnInfo(Map<String, ColumnInfoEntity> map) {
		Map<String, ColumnInfoEntity> map2 = new HashMap<String, ColumnInfoEntity>();
		for(String k : map.keySet()){
			ColumnInfoEntity e = map.get(k);
			ColumnInfoEntity t = new ColumnInfoEntity();
			EntityUtils.copyProperties(e, t);
			map2.put(k, t);
		}
		return map2;
	}

	/**
	 * 根据类，获取字段注解信息
	 * @param clazz
	 * @return
	 */
	public static Map<String, ColumnInfoEntity> getColumnInfos(Class<?> clazz) {
		String key = clazz.getName();
		Map<String, ColumnInfoEntity> infos = columnInfoMap.get(key);
		if (MapUtils.isEmpty(infos)) {
			initColumnInfos(clazz);
			infos = columnInfoMap.get(key);
		}
		return infos;
	}

	/**
	 * 根据类，获取字段属性信息
	 * @param clazz
	 * @return
	 */
	public static List<String> getEntityFiledNames(Class<?> clazz) {
		String key = clazz.getName();
		List<String> infos = entityInfoMap.get(key);
		if (infos == null) {
			initColumnProperties(clazz);
			infos = entityInfoMap.get(key);
		}
		List<String> ls = new ArrayList<String>();
		ls.addAll(infos);
		return ls;
	}

	/**
	 * 根据学校以及注解中学校类型限定条件，过滤掉不显示的字段
	 * @param clazz
	 * @param school
	 * @return
	 */
//	public static List<String> getEntityFiledNames(Class<?> clazz, School school) {
//		String key = clazz.getName();
//		List<String> infos = entityInfoMap.get(key);
//		if (infos == null) {
//			initColumnProperties(clazz);
//			infos = entityInfoMap.get(key);
//		}
//		List<String> ls = new ArrayList<String>();
//
//		if (school != null) {
//			Map<String, ColumnInfoEntity> columnInfo = getColumnInfos(clazz);
//			String schoolType = school.getSchoolType();
//			if (StringUtils.isNotBlank(schoolType)) {
//				for (String s : infos) {
//					ColumnInfoEntity m = columnInfo.get(s);
//					if (m != null) {
//						String v = m.getFitForSchoolType();
//						if (StringUtils.isNotBlank(v)) {
//							if (!schoolType.matches(v)) {
//								continue;
//							}
//						}
//					}
//					ls.add(s);
//				}
//			} else {
//				ls.addAll(infos);
//			}
//		} else {
//			ls.addAll(infos);
//		}
//		return ls;
//	}

	/**
	 * 获取注解信息
	 * @param clazz
	 * @param columnName
	 * @return
	 */
	public static ColumnInfoEntity getColumnInfo(Class<?> clazz, String columnName) {
		return getColumnInfos(clazz).get(columnName);
	}

}
