package net.zdsoft.syncdatamq.service.impl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.Transient;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.FileUtils;
import net.zdsoft.syncdatamq.service.BasedataSyncDataService;
import net.zdsoft.syncdatamq.utils.ActiveMQUtils;

@Service
public class BasedataSyncDataServiceImpl implements BasedataSyncDataService {

	private Logger log = Logger.getLogger(BasedataSyncDataServiceImpl.class);

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	private EntityManagerFactory createFactory(String name) {
		if (entityManagerFactory == null)
			entityManagerFactory = Persistence.createEntityManagerFactory(name);
		return entityManagerFactory;
	}

	@Override
	public void dealReceive() {
		String entities = Evn.getString("activemq.receive.entities");
		String clientId = Evn.getString("activemq.receive.client.id");

		if (StringUtils.isNotBlank(entities)) {

			ResourceLoader reader = new DefaultResourceLoader();
			Resource resource = reader.getResource("/conf/symData.json");
			try {
				final JSONObject allJson = JSONObject.parseObject(FileUtils.readString(resource.getInputStream()));
				final Map<String, Map<String, String>> typeMaps = new HashMap<String, Map<String, String>>();
				String[] ss = entities.split(",");
				for (String s : ss) {
					s = StringUtils.trim(s);
					final String entityFinal = s;
					final String queueName = clientId + "." + StringUtils.trim(s);
					new Thread(new Runnable() {
						@Override
						public void run() {
							String s = ActiveMQUtils.receiveMessageQueue(queueName);
							if(StringUtils.isBlank(s)){
								log.info(queueName + "消息队列中无数据！");
							}
							try {
								while (StringUtils.isNotBlank(s)) {
									dealMqText(entityFinal, s, allJson, typeMaps);
									ActiveMQUtils.commitSession(queueName);
									Thread.sleep(100);
									s = ActiveMQUtils.receiveMessageQueue(queueName);
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
								throw new RuntimeException(e);
							}
						}
					}).start();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		}
	}

	private void dealMqText(String entityName, String str, JSONObject allJson,
			Map<String, Map<String, String>> typeMaps) {
		if (allJson == null)
			return;

		final JSONObject symData = allJson.getJSONObject(entityName);
		if (symData == null) {
			throw new RuntimeException("symData.json文件中找不到对应的节点：" + entityName);
		}

		JSONArray columns = symData.getJSONArray("columns");
		Map<String, String> typeMap = typeMaps.get(entityName);
		if (typeMap == null || columns == null) {
			typeMap = new HashMap<String, String>();
			try {
				JSONArray onlyUseColumns = symData.getJSONArray("onlyUseColumns");
				if (onlyUseColumns != null) {
					columns = onlyUseColumns;
				} else {
					String entityNamePackage = entityName;
					if (!StringUtils.contains(entityName, ".")) {
						entityNamePackage = "net.zdsoft.basedata.entity." + entityName;
					}
					Object o = Class.forName(entityNamePackage).newInstance();
					String s = JSONObject.toJSONString(o, SerializerFeature.WriteMapNullValue);
					JSONObject entityJson = JSONObject.parseObject(s);

					Method[] methods = Class.forName(entityNamePackage).getDeclaredMethods();

					for (Method method : methods) {
						String methodName = method.getName();
						if (StringUtils.startsWith(methodName, "get")) {
							typeMap.put(StringUtils.lowerCase(StringUtils.substring(methodName, 3)),
									StringUtils.lowerCase(method.getReturnType().getSimpleName()));
						} else if (StringUtils.startsWith(methodName, "is")) {
							typeMap.put(StringUtils.lowerCase(StringUtils.substring(methodName, 2)),
									StringUtils.lowerCase(method.getReturnType().getSimpleName()));
						}
					}

					Map<String, String> columnMap = new HashMap<String, String>();
					Map<String, String> removeMap = new HashMap<String, String>();
					Field[] fields = Class.forName(entityNamePackage).getDeclaredFields();
					for (Field field : fields) {
						String name = field.getName();
						Column col = field.getAnnotation(Column.class);
						if (col != null && StringUtils.isNotBlank(col.name())) {
							columnMap.put(name, col.name());
						}

						Transient tran = field.getAnnotation(Transient.class);
						if (tran != null) {
							removeMap.put(name, name);
						}
					}
					
					JSONArray removeColumns = symData.getJSONArray("removeColumns");
					if(removeColumns != null){
						for(int index = 0; index < removeColumns.size(); index ++){
							JSONObject jo = removeColumns.getJSONObject(index);
							String en = jo.getString("entityName");
							if(StringUtils.isNotBlank(en)){
								removeMap.put(en, en);
							}
						}
					}
					
					JSONArray replaceColumns = symData.getJSONArray("replaceColumns");
					if(replaceColumns != null){
						for(int index = 0; index < replaceColumns.size(); index ++){
							JSONObject jo = replaceColumns.getJSONObject(index);
							String en = jo.getString("entityName");
							if(StringUtils.isNotBlank(en)){
								columnMap.put(en, jo.getString("columnName"));
							}
						}
					}

					columns = new JSONArray();
					for (String key : entityJson.keySet()) {
						if (removeMap.containsKey(key))
							continue;
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("columnName",
								columnMap.containsKey(key) ? columnMap.get(key) : EntityUtils.humpToLine(key));
						jsonObject.put("entityName", key);
						columns.add(jsonObject);
					}
				}
				symData.put("columns", columns);
				typeMaps.put(entityName, typeMap);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		String dbDriver = allJson.getString("dbDriver");
		String persistenceName = allJson.getString("persistenceName");
		EntityManagerFactory mysql = null;
		EntityManager em = null;
		try {
			String text = str;
			JSONArray array = JSONArray.parseArray(text);
			if (array.size() > 0) {
				mysql = createFactory(persistenceName);
				em = mysql.createEntityManager();
				em.getTransaction().begin();
				List<String> columnSets = new ArrayList<String>();
				List<String> insertColumns = new ArrayList<String>();
				// Map<String, String> columnKeys = new
				// HashMap<String, String>();
				List<Object> valueSets = new ArrayList<Object>();
				// List<Object> valueKeys = new ArrayList<Object>();
				Map<String, JSONObject> columnMap = new HashMap<String, JSONObject>();

				JSONArray jsonArray = symData.getJSONArray("columns");
				for (int index = 0; index < jsonArray.size(); index++) {
					JSONObject sc = jsonArray.getJSONObject(index);
					String columnEntityName = sc.getString("entityName");
					columnMap.put(columnEntityName, sc);
				}
				for (int i = 0; i < array.size(); i++) {
					String sql = "";
					if (StringUtils.equals(dbDriver, "mysql")) {
						sql = "REPLACE INTO " + symData.getString("tableName") + " SET ";
					} else if (StringUtils.equals(dbDriver, "oracle")) {
						sql = "merge into " + symData.getString("tableName") + " a using (select ? id from dual) b "
								+ "on (a.id = b.id) when matched then "
								+ "update set {update} when not matched then insert ({insert}) values ({insertValues})";
					}
					columnSets.clear();
					valueSets.clear();
					insertColumns.clear();
					JSONObject json = array.getJSONObject(i);
					for (String key : json.keySet()) {
						JSONObject sc = columnMap.get(key);
						if (sc != null) {
							String columnName = sc.getString("columnName");
							if (StringUtils.equals(dbDriver, "oracle")
									&& StringUtils.equalsIgnoreCase("id", columnName)) {
								continue;
							}
							String type = sc.getString("type");
							if (StringUtils.isBlank(type)) {
								type = StringUtils
										.lowerCase(typeMap.get(StringUtils.lowerCase(sc.getString("entityName"))));
							}
							String value = json.getString(key);
							Object object = null;
							if (ArrayUtils.contains(new String[] { "date", "datetime", "timestamp" }, type)) {
								if (NumberUtils.isDigits(value)) {
									object = new Date(NumberUtils.toLong(value));
								} else {
									object = DateUtils.parseDate(value);
								}
							} else if (ArrayUtils.contains(new String[] { "integer", "int" }, type)) {
								object = NumberUtils.toInt(value);
							} else if (StringUtils.equals("long", type)) {
								object = NumberUtils.toLong(value);
							} else {
								object = value;
							}
							insertColumns.add(columnName);
							columnSets.add(columnName + " = ?");
							valueSets.add(object);
						}
					}
					if (StringUtils.equals(dbDriver, "mysql")) {
						sql += StringUtils.join(columnSets, ",");
						Query query = em.createNativeQuery(sql);
						for (int index = 0; index < valueSets.size(); index++) {
							query.setParameter(index + 1, valueSets.get(index));
						}
						query.executeUpdate();
					} else if (StringUtils.equals(dbDriver, "oracle")) {
						sql = StringUtils.replace(sql, "{update}", StringUtils.join(columnSets, ","));
						insertColumns.add("id");
						sql = StringUtils.replace(sql, "{insert}", StringUtils.join(insertColumns, ","));
						sql = StringUtils.replace(sql, "{insertValues}",
								StringUtils.repeat("?", ",", insertColumns.size()));

						Query query = em.createNativeQuery(sql);
						query.setParameter(1, json.getString("id"));
						int count = 2;
						for (int index = 0; index < valueSets.size(); index++) {
							query.setParameter(count, valueSets.get(index));
							count++;
						}
						for (int index = 0; index < valueSets.size(); index++) {
							query.setParameter(count, valueSets.get(index));
							count++;
						}
						query.setParameter(count, json.getString("id"));
						query.executeUpdate();
					}
				}
				em.getTransaction().commit();
				log.info("更新" + entityName + "数据：" + array.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (em != null)
				em.close();
		}
	}

	/**
	 * @deprecated 暂时不用
	 * @param entityName
	 * @return
	 */
	private MessageListener dealListener(String entityName) {
		ResourceLoader reader = new DefaultResourceLoader();
		Resource resource = reader.getResource("/conf/symData.json");
		JSONObject json = null;
		try {
			json = JSONObject.parseObject(FileUtils.readString(resource.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (json == null)
			return null;

		final JSONObject symData = json.getJSONObject(entityName);
		if (symData == null) {
			return null;
		}

		JSONArray columns = symData.getJSONArray("columns");
		final Map<String, String> typeMap = new HashMap<String, String>();
		if (columns == null) {
			try {
				String entityNamePackage = entityName;
				if (!StringUtils.contains(entityName, ".")) {
					entityNamePackage = "net.zdsoft.basedata.entity." + entityName;
				}
				Object o = Class.forName(entityNamePackage).newInstance();
				String s = JSONObject.toJSONString(o, SerializerFeature.WriteMapNullValue);
				JSONObject entityJson = JSONObject.parseObject(s);

				Method[] methods = Class.forName(entityNamePackage).getDeclaredMethods();

				for (Method method : methods) {
					String methodName = method.getName();
					if (StringUtils.startsWith(methodName, "get")) {
						typeMap.put(StringUtils.lowerCase(StringUtils.substring(methodName, 3)),
								StringUtils.lowerCase(method.getReturnType().getSimpleName()));
					} else if (StringUtils.startsWith(methodName, "is")) {
						typeMap.put(StringUtils.lowerCase(StringUtils.substring(methodName, 2)),
								StringUtils.lowerCase(method.getReturnType().getSimpleName()));
					}
				}

				columns = new JSONArray();
				for (String key : entityJson.keySet()) {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("columnName", EntityUtils.humpToLine(key));
					jsonObject.put("entityName", key);
					columns.add(jsonObject);
				}
				symData.put("columns", columns);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		final String dbDriver = json.getString("dbDriver");
		final String entityNameFinal = entityName;
		final String persistenceName = json.getString("persistenceName");

		// TODO 根据entityName获取到SymTable对象
		return new MessageListener() {
			@Override
			public void onMessage(Message message) {
				EntityManagerFactory mysql = null;
				EntityManager em = null;
				try {
					String text = ((TextMessage) message).getText();
					JSONArray array = JSONArray.parseArray(text);
					if (array.size() > 0) {
						mysql = createFactory(persistenceName);
						em = mysql.createEntityManager();
						em.getTransaction().begin();
						List<String> columnSets = new ArrayList<String>();
						// Map<String, String> columnKeys = new
						// HashMap<String, String>();
						List<Object> valueSets = new ArrayList<Object>();
						// List<Object> valueKeys = new ArrayList<Object>();
						Map<String, JSONObject> columnMap = new HashMap<String, JSONObject>();

						JSONArray jsonArray = symData.getJSONArray("columns");
						for (int index = 0; index < jsonArray.size(); index++) {
							JSONObject sc = jsonArray.getJSONObject(index);
							String entityName = sc.getString("entityName");
							columnMap.put(entityName, sc);
						}
						for (int i = 0; i < array.size(); i++) {
							String sql = (StringUtils.equals(dbDriver, "mysql") ? "REPLACE " : "MERGER ") + " INTO "
									+ symData.getString("tableName") + " SET ";
							columnSets.clear();
							valueSets.clear();
							JSONObject json = array.getJSONObject(i);
							for (String key : json.keySet()) {
								JSONObject sc = columnMap.get(key);
								if (sc != null) {
									String columnName = sc.getString("columnName");
									String type = sc.getString("type");
									if (StringUtils.isBlank(type)) {
										type = StringUtils.lowerCase(
												typeMap.get(StringUtils.lowerCase(sc.getString("entityName"))));
									}
									String value = json.getString(key);
									Object object = null;
									if (ArrayUtils.contains(new String[] { "date", "datetime", "timestamp" }, type)) {
										if (NumberUtils.isDigits(value)) {
											object = new Date(NumberUtils.toLong(value));
										} else {
											object = DateUtils.parseDate(value);
										}
									} else if (ArrayUtils.contains(new String[] { "integer", "int" }, type)) {
										object = NumberUtils.toInt(value);
									} else if (StringUtils.equals("long", type)) {
										object = NumberUtils.toLong(value);
									} else {
										object = value;
									}
									// if (columnKeys.get(columnName) !=
									// null) {
									// valueKeys.add(object);
									// } else {
									columnSets.add(columnName + " = ?");
									valueSets.add(object);
									// }
								}
							}
							sql += StringUtils.join(columnSets, ",");
							// sql += " WHERE " +
							// StringUtils.join(columnKeys.values(), " AND
							// ");
							Query query = em.createNativeQuery(sql);
							for (int index = 0; index < valueSets.size(); index++) {
								query.setParameter(index + 1, valueSets.get(index));
							}
							// for (int index = valueSets.size(); index <
							// valueKeys.size() + valueSets.size(); index++)
							// {
							// query.setParameter(index + 1,
							// valueKeys.get(index - valueKeys.size()));
							// }
							query.executeUpdate();
						}
						em.getTransaction().commit();
						log.info("更新" + entityNameFinal + "数据：" + array.size());
					}
				} catch (JMSException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (em != null)
						em.close();
					// if (mysql != null)
					// mysql.close();
				}
			}
		};
	}

}
