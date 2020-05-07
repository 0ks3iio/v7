package net.zdsoft.framework.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.RedisInterface;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SingleRowMapper;

/**
 * @author linqz
 * @param <T>
 */
public abstract class BaseDao<T> extends BasicDAO {
	private DataFieldMaxValueIncrementer incre; // 主键值产生器

	private Class<T> clazz;
	/*
	 * 映射到数据库的字段
	 */
	private List<Field> fields;

	private String insertSqlTemplate;

	public BaseDao() {
		makeType();
		makeFields(null);
	}

	private void makeFields(Class<? extends Object> class2) {
		Class<? extends Object> class1 = class2;
		if (class1 == null) {
			class1 = clazz;
		}

		Table tanno = class1.getDeclaredAnnotation(Table.class);
		if (tanno == null) {
			if (class2 != null) {
				throw new RuntimeException(class1.getClass() + "不是可映射的实体类");
			}
			return;
		}
		String tableName = tanno.name();

		Field[] superFields = class1.getSuperclass().getDeclaredFields();
		Field[] declaredFields = class1.getDeclaredFields();
		List<Field> fieldList = new ArrayList<>(Arrays.asList(superFields));
		fieldList.addAll(Arrays.asList(declaredFields));

		List<String> columnNames = new ArrayList<>();
		fields = new ArrayList<>();
		for (Field field : fieldList) {
			if (field.isAnnotationPresent(Transient.class) || !Modifier.isPrivate(field.getModifiers())
					|| Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			String fieldName = field.getName();

			String regex = "([a-z])([A-Z])";
			String replacement = "$1_$2";
			fieldName = fieldName.replaceAll(regex, replacement).toLowerCase();
			
			columnNames.add(fieldName);  

			fields.add(field);
		}

		if (CollectionUtils.isEmpty(columnNames)) {
			throw new RuntimeException(class1.getClass() + "没有可以映射的字段");
		}

		String insertTemplate = "insert into %s (%s) values(%s)";
		String fieldNameStr = columnNames.stream().reduce((x, y) -> x + "," + y).get();
		String paramPlaceHolder = "";
		for (int i = 0; i < columnNames.size(); i++) {
			if (i != 0) {
				paramPlaceHolder += ",";
			}
			paramPlaceHolder += "?" + (i + 1);
		}
		insertSqlTemplate = String.format(insertTemplate, tableName, fieldNameStr, paramPlaceHolder);
	}

	private void makeType() {
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if (ParameterizedType.class.isAssignableFrom(genericSuperclass.getClass())) {
			clazz = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
		}
	}

	public String getString(ResultSet rs, String columnName) throws SQLException {
		return rs.getString(columnName);
	}

	public int getInt(ResultSet rs, String columnName) throws SQLException {
		return rs.getInt(columnName);
	}

	public long getLong(ResultSet rs, String columnName) throws SQLException {
		return rs.getLong(columnName);
	}

	public int getInt(ResultSet rs, int columnIndex) throws SQLException {
		return rs.getInt(columnIndex);
	}

	public float getFloat(ResultSet rs, String columnName) throws SQLException {
		return rs.getFloat(columnName);
	}

	public double getDouble(ResultSet rs, String columnName) throws SQLException {
		return rs.getDouble(columnName);
	}

	public Date getDate(ResultSet rs, String columnName) throws SQLException {
		return rs.getDate(columnName);
	}

	public Timestamp getTimestamp(ResultSet rs, String columnName) throws SQLException {
		return rs.getTimestamp(columnName);
	}

	/**
	 * 数据库字段与对象要严格一致。 性能比一般的写法要慢一点，适用于访问量不是很大的功能
	 * 
	 * @param rs
	 * @param t
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	protected T setFieldWithRs(ResultSet rs, T t) throws SQLException {
		Map<String, Integer> map = initColumnMap(rs);
		Method[] methods = t.getClass().getMethods();
		Map<String, Method> methodMap = new HashMap<String, Method>();
		for (Method m : methods) {
			String name = m.getName();
			if (!StringUtils.startsWith(name, "set")) {
				continue;
			}
			methodMap.put(m.getName(), m);
		}
		for (String columnName : map.keySet()) {
			String entityName = "";
			String[] ss = columnName.toLowerCase().split("_");
			for (String s : ss) {
				entityName += StringUtils.substring(s, 0, 1).toUpperCase() + StringUtils.substring(s, 1);
			}
			try {
				String name = "set" + entityName;
				Method m = methodMap.get(name);
				if (m == null) {
					continue;
				}
				Class[] classes = m.getParameterTypes();
				List<Object> args = new ArrayList<Object>();
				for (Class c : classes) {
					if (StringUtils.equals(c.getName(), "java.lang.String")) {
						args.add(rs.getString(columnName));
					} else if (StringUtils.equals(c.getName(), "java.lang.Integer")
							|| StringUtils.equals(c.getName(), "int")) {
						args.add(rs.getInt(columnName));
					} else if (StringUtils.equals(c.getName(), "java.lang.Double")
							|| StringUtils.equals(c.getName(), "double")) {
						args.add(rs.getDouble(columnName));
					} else if (StringUtils.equals(c.getName(), "java.lang.Float")
							|| StringUtils.equals(c.getName(), "float")) {
						args.add(rs.getFloat(columnName));
					} else if (StringUtils.equals(c.getName(), "java.lang.Long")
							|| StringUtils.equals(c.getName(), "long")) {
						args.add(rs.getLong(columnName));
					} else {
						args.add(rs.getObject(columnName));
					}
				}

				m.invoke(t, args.toArray(new Object[0]));
				// MethodUtils.invokeMethod(t, "set" +entityName,
				// rs.getObject(map.get(columnName)));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return t;
	}

	protected Map<String, Object> setFieldMapWithRs(final ResultSet rs, String verifyString) throws SQLException {
		Map<String, Integer> map = RedisUtils.getObject("COMMON.TABLE.COLUMN." + verifyString,
				RedisUtils.TIME_ONE_MONTH, new TypeReference<Map<String, Integer>>() {
				}, new RedisInterface<Map<String, Integer>>() {
					@Override
					public Map<String, Integer> queryData() {
						try {
							return initColumnMap(rs);
						} catch (SQLException e) {
							return new HashMap<String, Integer>();
						}
					}

				});

		Map<String, Object> retMap = new HashMap<String, Object>();
		int maxColumn = rs.getMetaData().getColumnCount();
		for (String columnName : map.keySet()) {
			int index = map.get(columnName);
			if (index > maxColumn) {
				continue;
			}
			retMap.put(columnName, rs.getObject(map.get(columnName)));
		}
		return retMap;
	}

	private Map<String, Integer> initColumnMap(ResultSet rs) throws SQLException {
		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
			String cn = resultSetMetaData.getColumnName(i);
			columnMap.put(cn.toUpperCase(), i);
		}
		return columnMap;
	}

	public void setIncre(DataFieldMaxValueIncrementer incre) {
		this.incre = incre;
	}

	public long getIncrementerKey() {
		return incre.nextLongValue();
	}

	public class MultiRow implements MultiRowMapper<T> {
		@Override
		public T mapRow(ResultSet rs, int numRow) throws SQLException {
			return setField(rs);
		}
	}

	public class MapRow implements MapRowMapper<String, T> {
		@Override
		public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("id");
		}

		@Override
		public T mapRowValue(ResultSet rs, int numRow) throws SQLException {
			return setField(rs);
		}
	}

	/**
	 * 自定义KEY的MapRow
	 * 
	 * @author zhaosf
	 * @version $Revision: 1.0 $, $Date: Oct 13, 2010 4:59:08 PM $
	 */
	public abstract class MapRowCustomKey implements MapRowMapper<String, T> {
		public abstract String getKey();

		@Override
		public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString(getKey());
		}

		@Override
		public T mapRowValue(ResultSet rs, int numRow) throws SQLException {
			return setField(rs);
		}
	}

	public class SingleRow implements SingleRowMapper<T> {
		@Override
		public T mapRow(ResultSet rs) throws SQLException {
			return setField(rs);
		}
	}

	public abstract T setField(ResultSet rs) throws SQLException;

	public long getUpdatestamp() {
		return System.currentTimeMillis();
	}

	protected Date getCreateDate() {
		return Calendar.getInstance().getTime();
	}

	protected Date getModifyDate() {
		return Calendar.getInstance().getTime();
	}

	protected Date getCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	public String andIn(List<Object> args, List<?> params, String columnName) {
		StringBuffer sql = new StringBuffer();
		sql.append(" and ").append(columnName).append(" in (");
		int i = 0;
		for (Object param : params) {
			args.add(param);
			if (i == 0) {
				sql.append("?");
			} else {
				sql.append(",?");
			}
			i++;
		}
		sql.append(")");
		return sql.toString();
	}

	public void saveAll(T[] ts) {
		if (ts == null || ts.length == 0) {
			return;
		}

		if (fields == null || !ts[0].getClass().isAssignableFrom(clazz)) {
			makeFields(ts[0].getClass());
		}

		List<Object[]> batchArgs = new ArrayList<>();
		Object[] obs;
		try {
			for (T t : ts) {
				obs = new Object[fields.size()];
				int index = 0;
				for (Field field : fields) {
					field.setAccessible(true);
					obs[index] = field.get(t);
					index++;
				}
				batchArgs.add(obs);
			}

			int start = 0;
			int end = 0;
			final int MAX_BATCH_SIZE = 1000;
			while (start < batchArgs.size()) {
				end = start + MAX_BATCH_SIZE;
				if (end > batchArgs.size()) {
					end = batchArgs.size();
				}
				List<Object[]> subList = batchArgs.subList(start, end);
				getJdbcTemplate().batchUpdate(insertSqlTemplate, subList);

				start = end;
			}

		}catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
