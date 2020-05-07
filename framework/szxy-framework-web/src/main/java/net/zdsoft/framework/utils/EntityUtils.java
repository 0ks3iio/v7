package net.zdsoft.framework.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EntityUtils {

	private static Pattern linePattern = Pattern.compile("_(\\w)");
	private static Pattern humpPattern = Pattern.compile("[A-Z]");

	/**
	 * 重组列表，根据列表中的某些属性，组成新的列表
	 * @deprecated 用java8的Stream来代替，提高效率
	 * @param os
	 *            原始列表
	 * @param xpath
	 *            列表属性，譬如： student.id 则最终组成的是id值的一个list
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public static <T, O> List<T> getList(List<O> os, String xpath) {
		if (CollectionUtils.isEmpty(os)) {
			return new ArrayList<>();
		}
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("list", os);
		return parser.parseExpression("#list.![#this." + xpath + "]").getValue(context, List.class);
	}
	
	public static <T, O> List<T> getList(List<O> os, Function<O, T> function) {
		return os.stream().map(function).collect(Collectors.toList());
//		if (CollectionUtils.isEmpty(os)) {
//			return new ArrayList<>();
//		}
//		ExpressionParser parser = new SpelExpressionParser();
//		EvaluationContext context = new StandardEvaluationContext();
//		context.setVariable("list", os);
//		return parser.parseExpression("#list.![#this." + xpath + "]").getValue(context, List.class);
	}

	/**
	 * 按照模板组装文字内容
	 * @param template
	 *            文字模板,学生#{studentName}，成绩#{score}
	 * @param os
	 *            符合模板占位符的对象（对象中包含了studentName和score字段）
	 * @return
	 */
	public static <O> List<String> getStrings(String template, O... os) {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expr3 = parser.parseExpression(template, new TemplateParserContext());
		List<String> list = new ArrayList<String>();
		for (O o : os) {
			list.add(expr3.getValue(o, String.class));
		}
		return list;
	}

	/**
	 * 按照模板组装文字内容
	 * @param template
	 *            文字模板,学生#{studentName}，成绩#{score}
	 * @param os
	 *            符合模板占位符的对象（对象中包含了studentName和score字段）
	 * @return
	 */
	public static <O> String getString(String template, O o) {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expr3 = parser.parseExpression(template, new TemplateParserContext());
		return expr3.getValue(o, String.class);
	}

	/**
	 * 重组列表，将列表中的某些属性，组成新的set
	 * @deprecated 用java8的Stream来代替，提高效率
	 * @param os
	 *            原始列表
	 * @param xpath
	 *            列表属性，譬如： student.id 则最终组成的是id值的一个set
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, O> Set<T> getSet(List<O> os, String xpath) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashSet<T>();
		}
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("list", os);
		Set<T> sets = parser.parseExpression("#list.![#this." + xpath + "]").getValue(context, Set.class);
		if (CollectionUtils.isNotEmpty(sets))
			sets.remove(null);
		return sets;
	}

	/**
	 * null值将被过滤
	 * @param os
	 * @param function
	 * @param <T>
	 * @param <O>
	 * @return
	 */
	public static <T, O> Set<T> getSet(List<O> os, Function<O, T> function) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashSet<T>();
		}
		return os.stream().map(function).filter(Objects::nonNull).collect(Collectors.toSet());
	}

	public static <T, O> T[] getArray(List<O> os, Function<O, T> function, IntFunction<T[]> generator) {
		return os.stream().map(function).toArray(generator);
	}

	/**
	 * 取出对象的某个属性值
	 * 
	 * @param o
	 *            对象
	 * @param xpath
	 *            属性路径， student.id
	 * @return
	 */
	@SuppressWarnings({ "hiding", "unchecked" })
	public static <T, O> T getValue(O o, String xpath) {
		if (o == null)
			return null;
		return (T) getValue(o, xpath, null);
	}

	/**
	 * 取出List中含有对应属性的对象
	 * 
	 * @param os
	 * @param xpath
	 * @param value
	 * @param <T>
	 * @return
	 */
	public static <T> T getValue(List<T> os, String xpath, String value) {
		try {
			if (!isContainValue(os, xpath, value)) {
				return null;
			}
			for (T o : os) {
				String prop = org.apache.commons.beanutils.BeanUtils.getProperty(o, xpath);

				if (StringUtils.equals(prop, value)) {
					return o;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return null;
	}

	/**
	 * 取出对象的某个属性，并做泛型转换
	 * 
	 * @param o
	 *            对象
	 * @param xpath
	 *            属性路径， student.id
	 * @param clazz
	 *            要转化的对象
	 * @return
	 */
	public static <T, O> T getValue(O o, String xpath, Class<T> clazz) {
		if (o == null)
			return null;
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("o", o);
		if (clazz == null) {
			return (T) parser.parseExpression("#o." + xpath).getValue(context);
		} else {
			return parser.parseExpression("#o." + xpath).getValue(context, clazz);
		}
	}

	public static <T, O> O setValue(O o, String xpath, Object value) {
		if (o == null)
			return null;
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("o", o);
		parser.parseExpression("#o." + xpath).setValue(context, value);
		return o;
	}

	/**
	 * 判断List中是否包含 指定属性是否为指定值的某个元素
	 * 
	 * @param os
	 * @param xpath
	 * @param value
	 * @return
	 */
	public static <T, O> boolean isContainValue(List<O> os, String xpath, String value) {
		if (CollectionUtils.isEmpty(os))
			return false;
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("list", os);
		return parser.parseExpression("#list.![#this." + xpath + "=='" + value + "']").getValue(context, Boolean.class);
	}

	/**
	 * @deprecated
	 * @param os
	 * @param keyXpath
	 * @return
	 */
	public static <O, K> Map<K, O> getMap(List<O> os, String keyXpath) {
		return getMap(os, keyXpath, null);
	}
	public static <O, K> Map<K, O> getMap(List<O> os, Function<O, K> keyFunction) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashMap<K, O>();
		}
		return os.stream().collect(Collectors.toMap(keyFunction, Function.identity(), (k1, k2) -> k1));
	}

	public static <O, K, V> Map<K, V> getMap(List<O> os, Function<O, K> keyFunction, Function<O, V> valueFunction) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashMap<K, V>();
		}
		return os.stream().collect(Collectors.toMap(keyFunction, valueFunction, (k1, k2) -> k1));
	}
	
	/**
	 * @deprecated
	 * @param os
	 * @param keyXpath
	 * @param valueXpath
	 * @return
	 */
	public static <O, K, V> Map<K, V> getMap(List<O> os, String keyXpath, String valueXpath) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashMap<K, V>();
		}
		
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("list", os);
		List<K> keys = parser
				.parseExpression("#list.![#this" + (StringUtils.isNotBlank(keyXpath) ? ("." + keyXpath) : "") + "]")
				.getValue(context, List.class);
		List<V> values;
		if (StringUtils.isBlank(valueXpath)) {
			values = parser.parseExpression("#list.![#this]").getValue(context, List.class);
		} else {
			values = parser.parseExpression("#list.![#this." + valueXpath + "]").getValue(context, List.class);
		}
		Map<K, V> map = new HashMap<K, V>();
		if (values.size() == keys.size()) {
			for (int i = 0; i < keys.size(); i++) {
				map.put(keys.get(i), values.get(i));
			}
		}
		return map;
	}

	public static <T> T getGeneric(Type type) {
		try {
			Type[] generics = ((ParameterizedType) type).getActualTypeArguments();
			@SuppressWarnings("unchecked")
			Class<T> mTClass = (Class<T>) (generics[0]);
			T t2 = (T) BeanUtils.instantiateClass(mTClass);
			return (T) t2;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getGenericClassName(Class<?> clazz) {
		return getGeneric(clazz).getClass().getSimpleName();
	}

	public static <T> T getGeneric(Class<?> clazz) {
		try {
			Type type = clazz.getGenericSuperclass();
			Type[] generics = ((ParameterizedType) type).getActualTypeArguments();
			@SuppressWarnings("unchecked")
			Class<T> mTClass = (Class<T>) (generics[0]);
			T t2 = (T) BeanUtils.instantiateClass(mTClass);
			return (T) t2;
		} catch (Exception e) {
			return null;
		}
	}

	public static String[] createArray(String... ss) {
		return ss;
	}

	public static <T> T[] toArray(Collection<T> os, Class<T> type) {
		int size = 0;
		T[] array = (T[]) Array.newInstance(type, CollectionUtils.size(os));
		if (CollectionUtils.isEmpty(os)) {
			return array;
		}
		return os.toArray(array);
	}

	/**
	 * 合并多个
	 * 
	 * @param os
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> merge(List<T>... os) {
		List<T> all = Lists.newArrayList();
		for (List<T> tList : os) {
			if (tList == null) {
				continue;
			}
			all.addAll(tList);
		}
		return all;
	}

	public static String underline2Camel(String param) {
		param = param.toLowerCase();
		Pattern p = Pattern.compile("_[a-z]");
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		while (mc.find()) {
			builder.replace(mc.start(), mc.end(), mc.group().toUpperCase().replaceFirst("_", ""));
			mc = p.matcher(builder.toString());
		}

		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

	/**
	 * 类属性复制
	 * 
	 * @param dest
	 * @param ori
	 * @param ignoreNull
	 *            ，如果为true，则将源对象中属性为null的值不进行复制，
	 *            如果是整个对象属性一致的，则调用其他copyProperties方法，以提高效率
	 * @return
	 */
	public static <T, K> K copyProperties(T source, K target, boolean ignoreNull) {
		if (source == null)
			return target;
		if (target == null)
			return null;

		if (!ignoreNull)
			return copyProperties(source, target);
		else {
			BeanMap map = BeanMap.create(BeanUtils.instantiateClass(target.getClass()));
			map.setBean(source);

			List<String> getters = new ArrayList<String>();

			for (Object key : map.keySet()) {
				Object v = map.get(key);
				if (v == null) {
					getters.add(key.toString());
				}
			}
			BeanUtils.copyProperties(source, target, getters.toArray(new String[0]));
			return target;
		}
	}

	/**
	 * 复制对象属性
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	public static <T, K> K copyProperties(T source, K target) {
		if (source == null)
			return target;
		if (target == null)
			return null;

		BeanCopier bc = BeanCopier.create(source.getClass(), target.getClass(), false);
		try {
			bc.copy(source, target, null);
			return target;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 复制对象属性
	 * 
	 * @param source
	 * @param classK
	 * @return
	 */
	public static <T, K> K copyProperties(T source, Class<K> targetClass) {
		if (source == null)
			return null;
		BeanCopier bc = BeanCopier.create(source.getClass(), targetClass, false);
		try {
			K k = BeanUtils.instantiateClass(targetClass);
			bc.copy(source, k, null);
			return k;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 批量复制对象属性
	 * 
	 * @param dest
	 * @param classt
	 * @param classK
	 * @return
	 */
	public static <T, K> List<K> copyProperties(List<T> source, Class<T> resourceClass, Class<K> targetClass) {
		if (CollectionUtils.isEmpty(source))
			return new ArrayList<K>();
		BeanCopier bc = BeanCopier.create(resourceClass, targetClass, false);
		List<K> list = new ArrayList<K>();
		try {
			for (T t : source) {
				K k = BeanUtils.instantiateClass(targetClass);
				bc.copy(t, k, null);
				list.add(k);
			}
			return list;
		} catch (Exception e) {
			return new ArrayList<K>();
		}
	}

	/**
	 * 对数据进行位置变更操作
	 * 
	 * @param list
	 * @param t
	 * @param upSize
	 *            移动的位置，可以为负数
	 * @return
	 */
	public static <T> List<T> changePosition(List<T> list, T t, int upSize) {
		if (upSize == 0) {
			return list;
		}
		int index = list.indexOf(t);
		if (index < 0) {
			return list;
		}
		list.remove(t);
		list.add(index + upSize < 0 ? 0 : index + upSize, t);
		return list;
	}

	public static <T> List<T> removeEmptyElement(T[] elements) {
		return removeEmptyElement(Arrays.asList(elements));
	}

	public static <K, V> Map<K, V> removeEmptyElement(Map<K, V> maps) {
		if (MapUtils.isEmpty(maps)) {
			return Maps.newHashMap();
		}
		for (Iterator<Map.Entry<K, V>> it = maps.entrySet().iterator(); it.hasNext();) {
			Map.Entry<K, V> kvEntry = it.next();
			if (kvEntry.getValue() == null) {
				it.remove();
				continue;
			}
			if (kvEntry.getValue() instanceof String && StringUtils.isBlank(String.class.cast(kvEntry.getValue()))) {
				it.remove();
			}
		}
		return maps;
	}

	public static <T> List<T> removeEmptyElement(List<T> elements) {
		if (CollectionUtils.isEmpty(elements)) {
			return Lists.newArrayList();
		}
		List<T> os = Lists.newArrayList();
		for (T t : elements) {
			if (t != null) {
				if (t instanceof String) {
					if (StringUtils.isNotEmpty((String) t)) {
						os.add(t);
					}
					continue;
				}
				os.add(t);
			}
		}
		return os;
	}

	/**
	 *
	 * @param elements
	 * @param type
	 * @param <T>
	 * @return
	 **/
	public static <T> T[] removeEmptyElement(T[] elements, Class<T> type) {
		T[] array = (T[]) Array.newInstance(type, 1);
		List<T> os = removeEmptyElement(elements);
		return os.toArray(array);
	}

	public static <T> List<T> filter2(List<T> os, Predicate<T> filter) {
		return os.stream().filter(filter).collect(Collectors.toList());
	}
	/**
	 * 对集合的内容进行过滤
	 *@deprecated 尽量用filter2
	 * @param os
	 * @param filter
	 *            过滤器，=true，表示要排除的
	 * @return
	 */
	public static <T> List<T> filter(List<T> os, Filter<T> filter) {
		List<T> nOs = Lists.newArrayList();
		if (CollectionUtils.isEmpty(os)) {
			return nOs;
		}
		for (T o : os) {
			if (!filter.doFilter(o)) {
				nOs.add(o);
			}
		}
		os = nOs;
		return nOs;
	}

	public interface Filter<T> {
		boolean doFilter(T t);
	}

	private static <T> boolean checkNotNull(T t) {
		if (t == null) {
			throw new IllegalArgumentException("can not null");
		}
		return true;
	}

	/**
	 * 获取对象的标志信息（不同的内容会产生惟一标识信息）
	 * 
	 * @param data
	 * @return
	 */
	public static String getCode(String data) {
		return DigestUtils.md5Hex(data) + DigestUtils.shaHex(data);
	}

	public static String getCode(String[] datas) {
		String data = JSONObject.toJSONString(datas);
		return DigestUtils.md5Hex(data) + DigestUtils.shaHex(data);
	}

	/**
	 * md5(获取对象的标志信息（不同的内容会产生惟一标识信息））
	 * 
	 * @param data
	 * @return
	 */
	public static String getCode32(String data) {
		return DigestUtils.md5Hex(DigestUtils.md5Hex(data) + DigestUtils.shaHex(data));
	}

	/**
	 * 通用实体转换方法,将JPA返回的数组转化成对应的实体集合,这里通过泛型和反射实现
	 * 
	 * @param <T>
	 * @param list
	 * @param clazz
	 *            需要转化后的类型
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) throws Exception {
		List<T> returnList = new ArrayList<T>();
		Object[] co = list.get(0);
		Class[] c2 = new Class[co.length];

		// 确定构造方法
		for (int i = 0; i < co.length; i++) {
			c2[i] = co[i].getClass();
		}

		for (Object[] o : list) {
			Constructor<T> constructor = clazz.getConstructor(c2);
			returnList.add(constructor.newInstance(o));
		}

		return returnList;
	}

	/**
	 * null return Empty <br/>
	 * use Number.toString();
	 * 
	 * @param numer
	 * @return
	 */
	public static String toString(Number numer) {

		return numer == null ? StringUtils.EMPTY : numer.toString();
	}
	
	
	public static <O, K, V> Map<K, List<V>> getListMap(List<O> os, Function<O, K> keyFunction, 
			Function<O, V> valueFunction) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashMap<K, List<V>>();
		}
		return os.stream().collect(Collectors.groupingBy(keyFunction, Collectors.mapping(valueFunction, Collectors.toList())));
	}

	/**
	 * @deprecated
	 * @param os
	 * @param keyXpath
	 * @param valueXpath
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <O, K, V> Map<K, V> getListMap(List<O> os, String keyXpath, String valueXpath) {
		if (CollectionUtils.isEmpty(os)) {
			return new HashMap<K, V>();
		}
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext();
		context.setVariable("list", os);
		List<K> keys = parser
				.parseExpression("#list.![#this" + (StringUtils.isNotBlank(keyXpath) ? ("." + keyXpath) : "") + "]")
				.getValue(context, List.class);
		List<V> values;
		if (StringUtils.isBlank(valueXpath)) {
			values = parser.parseExpression("#list.![#this]").getValue(context, List.class);
		} else {
			values = parser.parseExpression("#list.![#this." + valueXpath + "]").getValue(context, List.class);
		}
		Map<K, V> map = new HashMap<K, V>();
		if (values.size() == keys.size()) {
			for (int i = 0; i < keys.size(); i++) {
				List<V> vaList = new ArrayList<V>();
				List<V> vaList2 = new ArrayList<V>();
				for (int j = 0; j < keys.size(); j++) {
					if (keys.get(i).equals(keys.get(j)))

						vaList.add(values.get(j));

				}
				if (vaList.size() > 1)
					vaList2 = vaList;
				else {
					vaList2.add(values.get(i));
				}

				map.put(keys.get(i), (V) vaList2);
			}
		}
		return (Map<K, V>) map;
	}

	/** 下划线转驼峰 */
	public static String lineToHump(String str) {
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/** 驼峰转下划线 */
	public static String humpToLine(String str) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	public static void invoke(Object o, String expression) {
		ExpressionParser parser = new SpelExpressionParser();
		EvaluationContext context = new StandardEvaluationContext(o);
		parser.parseExpression(expression).getValue(context);
	}
	
	public static List<String> createNumList(int size){
		ArrayList<String> list = new ArrayList<>();
		if(size<0)
			return list;
		for(int i=0;i<size;i++){
			list.add(String.valueOf(i+1));
		}
		return list;
	}
	
	public static <T> T ifNone(T t, T defaultT) {
		if(t != null)
			return t;
		return defaultT;
	}
}
