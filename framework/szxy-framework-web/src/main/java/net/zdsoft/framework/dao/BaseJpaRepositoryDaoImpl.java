package net.zdsoft.framework.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.google.common.collect.Lists;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;

public class BaseJpaRepositoryDaoImpl<T extends BaseEntity<K>, K extends Serializable>
		extends SimpleJpaRepository<T, Serializable> implements BaseJpaRepositoryDao<T, Serializable> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseJpaRepositoryDaoImpl.class);

	private final EntityManager entityManager;
	private JpaEntityInformation<T, ?> entityInformation;

	public BaseJpaRepositoryDaoImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}

	public BaseJpaRepositoryDaoImpl(final JpaEntityInformation<T, ?> entityInformation,
			final EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
	}

	// @Override
	public void update(T t, String id, String[] properties) {
		String sql = "UPDATE " + t.getClass().getName() + " set ";

		List<String> ps = new ArrayList<String>();
		for (int i = 0; i < properties.length; i++) {
			ps.add(properties[i] + " = ?" + (i + 1));
		}
		sql += StringUtils.join(ps.toArray(new String[0]), ",");
		sql += " WHERE id = ?" + (properties.length + 1);
		Query query = entityManager.createQuery(sql);
		for (int i = 0; i < properties.length; i++) {
			query.setParameter((i + 1), EntityUtils.getValue(t, properties[i]));
		}
		query.setParameter((properties.length + 1), id);
		query.executeUpdate();
	}

	@Override
	public void update(T t, Serializable id, String[] properties) {
		update(t, id.toString(), properties);
	}
	
	@Override
	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params, String inName, Object[] inArgs, String[] properties) {
		if (ArrayUtils.isEmpty(properties))
			return new ArrayList<>();
		String sql = "select ";
		
		List<String> ps = new ArrayList<String>();
		for (int i = 0; i < properties.length; i++) {
			ps.add(properties[i]);
		}
		sql += StringUtils.join(ps.toArray(new String[0]), ",");
		sql += " FROM " + clazz.getName() + " WHERE ";
		
		List<String> ns = new ArrayList<String>();
		if(ArrayUtils.isNotEmpty(names)) {
			for (int i = 0; i < names.length; i++) {
				ns.add(names[i] + " = ?" + (i + 1));
			}
			sql += StringUtils.join(ns, " AND ");
		}
		
		int length = ArrayUtils.getLength(inArgs);
		if(length <= 0) {
			return new ArrayList<>();
		}
		List<String> andSql = new ArrayList<>();
		int cyc = length / 1000 + (length % 1000 > 0 ? 1 : 0);
		if(length > 0) {
			for(int i = 0; i < cyc; i ++) {
				andSql.add(inName + " IN (?" + (ns.size() + i+1) + ") ");
			}
		}
			 
		if(ArrayUtils.isNotEmpty(names)) {
			sql += " AND ";
		}
		if(CollectionUtils.isNotEmpty(andSql)) {	
			sql += " (";
			sql += StringUtils.join(andSql, " OR ");
			sql += ") ";
		}
		
		Query query = entityManager.createQuery(sql);
		if(ArrayUtils.isNotEmpty(params)) {
			for(int i = 0; i < params.length; i ++) {
				query.setParameter(i + 1, params[i]);
			}
		}
		for(int i = 0; i < cyc; i ++) {
			query.setParameter(ArrayUtils.getLength(params) + i + 1, Arrays.asList(ArrayUtils.subarray(inArgs, i * 1000, (i + 1)*1000 > length ? length : (i + 1) * 1000)));
		}
		
		List<T> ts = new ArrayList<>();
		
		if (properties.length > 1) {
			List<Object[]> oes = (List<Object[]>) query.getResultList();
			for(Object[] os : oes){
				T t = createTClass(clazz);
				ts.add(t);
				for (int i = 0; i < properties.length; i++) {
					EntityUtils.setValue(t, properties[i], os[i]);
				}
			}
		} else {
			List<Object> oss = query.getResultList();
			for(Object os : oss){
				T t = createTClass(clazz);
				ts.add(t);
				EntityUtils.setValue(t, properties[0], os);
			}
		}
		return ts;
	}

	public List<T> findListBy(Class<T> clazz, String[] names, Object[] params, String[] properties) {
		if (ArrayUtils.isEmpty(properties))
			return new ArrayList<>();
		String sql = "select ";
		
		List<String> ps = new ArrayList<String>();
		for (int i = 0; i < properties.length; i++) {
			ps.add(properties[i]);
		}
		sql += StringUtils.join(ps.toArray(new String[0]), ",");
		sql += " FROM " + clazz.getName() + " WHERE ";
		
		List<String> ns = new ArrayList<String>();
		for (int i = 0; i < names.length; i++) {
			ns.add(names[i]  + "= ?" + (i + 1));
		}
		sql += StringUtils.join(ns, " AND ");
		
		Query query = entityManager.createQuery(sql);
		for(int i = 0; i < params.length; i ++) {
			query.setParameter(i + 1, params[i]);
		}
		
		List<T> ts = new ArrayList<>();
		
		if (properties.length > 1) {
			List<Object[]> oes = (List<Object[]>) query.getResultList();
			for(Object[] os : oes){
				T t = createTClass(clazz);
				ts.add(t);
				for (int i = 0; i < properties.length; i++) {
					EntityUtils.setValue(t, properties[i], os[i]);
				}
			}
		} else {
			List<Object> oss = query.getResultList();
			for(Object os : oss){
				T t = createTClass(clazz);
				ts.add(t);
				EntityUtils.setValue(t, properties[0], os);
			}
		}
		return ts;
	}

	private T createTClass(Class<T> clazz) {
		T t;
		try {
			t = clazz.newInstance();
		} catch (Exception e) {
			return null;
		}
		return t;
	}

	@Override
	public T findOne(Class<T> clazz, String id, String[] properties) {
		if (ArrayUtils.isEmpty(properties))
			return null;

		String sql = "select ";

		List<String> ps = new ArrayList<String>();
		for (int i = 0; i < properties.length; i++) {
			ps.add(properties[i]);
		}
		sql += StringUtils.join(ps.toArray(new String[0]), ",");
		sql += " FROM " + clazz.getName() + " WHERE id = ?1";
		Query query = entityManager.createQuery(sql);
		query.setParameter(1, id);

		T t;
		try {
			t = clazz.newInstance();
		} catch (Exception e) {
			return null;
		}

		if (t == null)
			return null;

		if (properties.length > 1) {
			List<Object[]> oes = (List<Object[]>) query.getResultList();
			if(oes.size() > 0){
				for (int i = 0; i < properties.length; i++) {
					EntityUtils.setValue(t, properties[i], oes.get(0)[i]);
				}
				return t;
			}
			else{
				return null;
			}
		} else {
			List<Object> os = query.getResultList();
			if(os.size() > 0)
				return EntityUtils.setValue(t, properties[0], os.get(0));
			else
				return null;
		}
	}

	@Override
	public void update(T t, String[] properties) {
		String id = EntityUtils.getValue(t, "id");
		update(t, id, properties);
	}

	@Override
	public int maxIntId(Class<T> clazz) {
		return maxIntId(clazz.getName());
	}

	@Override
	public int maxIntId(String claName) {
		String sql = "select max(id) from " + claName;
		Query query = entityManager.createQuery(sql, Integer.class);
		Integer o = (Integer) query.getSingleResult();
		if (o == null) {
			return 0;
		} else {
			return o;
		}
	}

	@Override
	public <W, V> void update(Map<W, V> whereAndVal, String whereXpath, String valueXpath) {
		StringBuilder sb;
		for (Map.Entry<W, V> entry : whereAndVal.entrySet()) {
			sb = new StringBuilder();
			if (entry.getValue() == null) {
				LOGGER.error("[update set " + valueXpath + "=? where " + whereXpath + "=?] value is null whereVal is"
						+ entry.getKey());
				continue;
			}
			if (entry.getKey() == null) {
				LOGGER.error("[update set " + valueXpath + "=? where " + whereXpath + "=?] whereValue is null value is"
						+ entry.getValue());
				continue;
			}
			sb.append("update ").append(entityInformation.getEntityName()).append(" set ").append(valueXpath)
					.append("='").append(entry.getValue()).append("'").append(" where ").append(whereXpath).append("='")
					.append(entry.getKey()).append("'");
			entityManager.createQuery(sb.toString()).executeUpdate();
		}
	}

	@Override
	public String findData(String entityName, String... ids) {
		String sql = "From " + entityName + " where id IN (?1)";
		return SUtils.s(entityManager.createQuery(sql).setParameter(1, Arrays.asList(ids)).getResultList());
	}

	@Override
	public String findData(String entityName, Date modifyTime) {
		return findData(entityName, modifyTime, "modifyTime", 2000);
	}

	@Override
	public String findData(String entityName, Date modifyTime, String modifyTimeName, int fetchSize) {
		String sql = "From " + entityName + " where " + modifyTimeName + " > ?1 Order By " + modifyTimeName;
		Query query = entityManager.createQuery(sql).setParameter(1, modifyTime);
		query.setFirstResult(0);
		query.setMaxResults(fetchSize);
		List list = query.getResultList();
		if (list.size() < fetchSize) {
			return SUtils.s(list);
		}

		Date lastDate = null;
		for (int i = list.size() - 1; i >= 0; i--) {
			Object o = list.get(i);
			if (lastDate == null)
				lastDate = EntityUtils.getValue(o, modifyTimeName);
			else {
				Date dateTime = EntityUtils.getValue(o, modifyTimeName);
				int com = dateTime.compareTo(lastDate);
				if (com != 0) {
					return SUtils.s(list.subList(0, i + 1));
				}
				lastDate = dateTime;
			}
		}
		return SUtils.s(list);
	}
}
